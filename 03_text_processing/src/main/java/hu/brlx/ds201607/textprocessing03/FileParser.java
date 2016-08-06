package hu.brlx.ds201607.textprocessing03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser implements Runnable {

    private Map<String, BlockingQueue<String>> queueMap;

    private File inFile;
    private Map<String, Pattern> patternMap;

    public FileParser(Map<String, BlockingQueue<String>> queueMap, File inFile, Map<String, Pattern> patternMap) {
        this.queueMap = queueMap;
        this.inFile = inFile;
        this.patternMap = patternMap;
    }

    public void run() {
//        System.out.println("FileParser - " + inFile.getName() + " - run()");
        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(inFile));
            for (String line; (line = buffReader.readLine()) != null;) {
//                System.out.println("reading line from " + inFile.getName() + ": '" + line + "'");
                line = line.trim();
                if (line.equals("")) {
//                    System.out.println("  empty line, skipping");
                    continue;
                }
                String matchedPatternName = null;
                for (Map.Entry<String, Pattern> entry : patternMap.entrySet()) {
                    final Matcher matcher = entry.getValue().matcher(line);
                    final boolean found = matcher.find();
                    if (found) {
                        matchedPatternName = entry.getKey();
                        break;
                    }
                }
                if (matchedPatternName != null) {
                    try {
//                        System.out.println("    putting line into queue(" + matchedPatternName + "): " + line);
                        queueMap.get(matchedPatternName).put(line);
                    } catch (InterruptedException e) {
                        System.out.printf("ERROR: interrupted when trying to store line: '" + line + "'");
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("line not matched, skipping: '" + line + "'");
                }
            }
//            System.out.println("FileParser - " + inFile.getName() + ": finished");
        } catch (IOException e) {
            System.out.println("ERROR when reading from file " + inFile.getName());
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "FileParser [" +
                "inFile=" + (inFile != null ? inFile.getName() : "null") +
                ']';
    }
}

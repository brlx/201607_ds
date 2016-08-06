package hu.brlx.ds201607.textprocessing03;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {

    private static final String SEPARATOR_PATTERN = ":";
    private static final String SEPARATOR_PATH = "/";
    private static final int QUEUE_DEPTH = 50;

    private Map<String, Pattern> patternMap;
    private Map<String, BlockingQueue<String>> queueMap;
    private Map<String, String> rawPatternMap;
    private Map<String, File> outFileMap;
    private List<File> inFiles;

    private Map<String, Thread> parserThreads = new HashMap<>();
    private Map<String, Thread> writerThreads = new HashMap<>();
    private String outFolder;

    public Controller(List<File> inFiles, List<String> rawPatterns, String outFolder) {
        this.queueMap = new HashMap<>();
        this.outFileMap = new HashMap<>();
        this.inFiles = inFiles;
        this.outFolder = outFolder;
        patternMap = new HashMap<>();
        rawPatternMap = new HashMap<>();
        this.rawPatternMap = rawPatterns.stream()
                .map(pattern -> pattern.split(SEPARATOR_PATTERN, 2))
                .peek(parts -> {
                    queueMap.put(parts[0], new ArrayBlockingQueue<>(QUEUE_DEPTH, true));
                    outFileMap.put(parts[0], new File(outFolder + SEPARATOR_PATH + parts[0] + ".txt"));
                    patternMap.put(parts[0], Pattern.compile(parts[1].replaceFirst(" ", "")));
                    System.out.println("Registering pattern, name=" + parts[0] + ", pattern=" + parts[1]);
                })
                .collect(Collectors.toMap(p -> p[0], p -> p[1]));
    }

    public void startProcessing() {
        inFiles.forEach(file -> {
            final FileParser fileParser = new FileParser(queueMap, file, patternMap);
            final Thread thread = new Thread(fileParser);
            thread.setName("FileParser-" + file.getName());
            parserThreads.put(file.getName(), thread);
            thread.start();
        });

        rawPatternMap.entrySet().forEach(entry -> {
            final String patternName = entry.getKey();
            final File outFile = outFileMap.get(patternName);
            final FileOutputter fileOutputter = new FileOutputter(queueMap.get(patternName), outFile, patternName);
            final Thread thread = new Thread(fileOutputter);
            thread.setName("FileOutputter-" + outFile.getName());
            writerThreads.put(patternName, thread);
            thread.start();
        });

        parserThreads.values().forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException ignore) {
            }
        });

        writerThreads.values().forEach(Thread::interrupt);
        writerThreads.values().forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException ignore) {
            }
        });
    }
}

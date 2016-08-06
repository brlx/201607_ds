package hu.brlx.ds201607.textprocessing03;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextProcessor {

    private static final String USAGE = "Usage:\n first param:  the input folder,\n second param: the config file with the patterns,\n third param:  the output folder";

    public static void main(String args[]) {
        System.out.println("TextProcessor main");
        System.out.println("params:");
        for (String arg : args) {
            System.out.println("  " + arg);
        }
        if (args.length < 3) {
            System.out.println(USAGE);
            return;
        }

        final String inputAbsPath = args[0].trim();
        final String patternFilePath = args[1].trim();
        final String outFolderPath = args[2].trim();
        if (inputAbsPath.equals("") || patternFilePath.equals("")) {
            System.out.println(USAGE);
            return;
        }
        File inFolder = new File(inputAbsPath);
        if (!inFolder.exists() || !inFolder.isDirectory()) {
            System.out.println("The input folder could not be found.");
            return;
        }

        final List<File> inFiles = Arrays.asList(inFolder.listFiles());

        final File patternFile = new File(patternFilePath);
        if (!patternFile.exists() || patternFile.isDirectory()) {
            System.out.println("The configuration file with the patterns could not be found.");
            return;
        }

        List<String> rawPatterns = new ArrayList<>();
        BufferedReader buffReader = null;
        try {
            final FileReader fileReader = new FileReader(patternFile);
            buffReader = new BufferedReader(fileReader);
            for (String line; (line = buffReader.readLine()) != null;) {
                rawPatterns.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("The configuration file with the patterns could not be found.");
            return;
        } catch (IOException e) {
            System.out.println("The configuration file with the patterns could not be read.");
            return;
        }

        if (rawPatterns.size() < 1) {
            System.out.println("There are not patterns in the config file.");
            return;
        }

        final File outFolder = new File(outFolderPath);
        if (!outFolder.exists() || !outFolder.isDirectory()) {
            System.out.println("The input folder could not be found.");
            return;
        }

        System.out.println("TextProcessor starting...");
        final Controller controller = new Controller(inFiles, rawPatterns, outFolderPath);
        controller.startProcessing();
        System.out.println("TextProcessor finished");
    }

}

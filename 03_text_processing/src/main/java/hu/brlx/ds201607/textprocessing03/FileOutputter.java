package hu.brlx.ds201607.textprocessing03;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class FileOutputter implements Runnable {

    private BlockingQueue<String> queue;
    private File outFile;
    private String patternName;

    public FileOutputter(BlockingQueue<String> queue, File outFile, String name) {
        this.queue = queue;
        this.outFile = outFile;
        this.patternName = name;
    }

    public void run() {
        System.out.println("For the pattern '" + patternName + "' using file '" + outFile.getAbsolutePath() + "'");
        try (FileWriter file = new FileWriter(outFile);
             BufferedWriter bufferedWriter = new BufferedWriter(file);) {

            while (true) {
                try {
                    final String line = queue.take();
//                    System.out.println("FileOutputter - new line: '" + line + "'");
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                } catch (InterruptedException e) {
//                    System.out.println("FileOutputter - " + patternName + " - interrupted, exiting...");
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR when writing to the file");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "FileOutputter [" +
                "patternName='" + patternName + '\'' +
                ']';
    }
}

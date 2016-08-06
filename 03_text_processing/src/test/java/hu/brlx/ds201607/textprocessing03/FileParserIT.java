package hu.brlx.ds201607.textprocessing03;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileParserIT {

    private static final String OUTFOLDER = "target";
    private static final String PATH_CONFIG = "/config/config.txt";
    private static final String[] PATHS_INPUT = new String[]{"/log1.txt", "/log2.txt", "/log3.txt"};

    private Controller cut;

    private List<File> inputFiles;
    private List<String> rawPatterns;
    private String outFolderPath;

    @Test
    public void testWithData() throws IOException {
        givenData();
        cut = new Controller(inputFiles, rawPatterns, outFolderPath);
        cut.startProcessing();
    }

    private void givenData() throws IOException {
        inputFiles = new ArrayList<>();
        rawPatterns = new ArrayList<>();

//        final URL resource = getClass().getResource(".");
//        System.out.println("working dir = " + resource);

        URL url = getClass().getResource(PATH_CONFIG);
        File file = new File(url.getFile());
        BufferedReader buffReader = new BufferedReader(new FileReader(file));
        for (String line; (line = buffReader.readLine()) != null; ) {
            rawPatterns.add(line);
        }
        for (String path : PATHS_INPUT) {
            url = getClass().getResource(path);
            inputFiles.add(new File(url.getFile()));
        }

        url = getClass().getResource(".");
        outFolderPath = url.getPath() + OUTFOLDER;

        File newOutFolder = new File(outFolderPath);
        newOutFolder.mkdir();
    }

}

package hu.brlx.ds201607.foldertree02;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TreeFiltererIT {

    private static final String PATH_ORIGINAL_TREE = "/original_list.txt";
    private static final String PATH_READABLE_TREE = "/readable_list.txt";
    private static final String PATH_WRITABLE_TREE = "/writable_list.txt";

    private static final String FOLDER_SEPARATOR = "/";

    private TreeItem tree;
    private TreeFilterer cut;
    private List<String[]> writablesList;
    private List<String[]> readablesList;

    @Before
    public void before() throws IOException {
        cut = new TreeFilterer();
        tree = new TreeItem("/");

        URL url = getClass().getResource(PATH_ORIGINAL_TREE);
        File file = new File(url.getFile());
        BufferedReader buffReader = new BufferedReader(new FileReader(file));
        for (String line; (line = buffReader.readLine()) != null;) {
            if (isLineValid(line)) {
                tree.addSubtree(line.replaceFirst(FOLDER_SEPARATOR, "").split(FOLDER_SEPARATOR));
            }
        }

        readablesList = new ArrayList<>();
        url = getClass().getResource(PATH_READABLE_TREE);
        file = new File(url.getFile());
        buffReader = new BufferedReader(new FileReader(file));
        for (String line; (line = buffReader.readLine()) != null;) {
            if (isLineValid(line)) {
                readablesList.add(line.replaceFirst(FOLDER_SEPARATOR, "").split(FOLDER_SEPARATOR));
            }
        }

        writablesList = new ArrayList<>();
        url = getClass().getResource(PATH_WRITABLE_TREE);
        file = new File(url.getFile());
        buffReader = new BufferedReader(new FileReader(file));
        for (String line; (line = buffReader.readLine()) != null;) {
            if (isLineValid(line)) {
                writablesList.add(line.replaceFirst(FOLDER_SEPARATOR, "").split(FOLDER_SEPARATOR));
            }
        }
    }

    @Test
    public void testFilterTreeForUser() throws Exception {
        System.out.println("testFilterTreeForUser START");

        final TreeItem filteredTree = cut.filterTreeForUser(tree, readablesList, writablesList);

        System.out.println("==========");
        filteredTree.print().forEach(System.out::println);

        System.out.println("testFilterTreeForUser FINISH");
    }

    private boolean isLineValid(String line) {
        return !line.trim().equals("") && !line.startsWith("#");
    }

}

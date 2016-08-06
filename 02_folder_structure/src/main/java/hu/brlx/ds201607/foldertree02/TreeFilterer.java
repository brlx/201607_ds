package hu.brlx.ds201607.foldertree02;

import java.util.List;

public class TreeFilterer {

    public TreeItem filterTreeForUser(TreeItem root, List<String[]> readables, List<String[]> writables) {
        readables.forEach(root::applyReadableSubtree);
        root.purgeUnreadable();
        writables.forEach(root::applyWritableSubtree);
        root.purgeUnneeded();
        return root;
    }

}

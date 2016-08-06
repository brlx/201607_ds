package hu.brlx.ds201607.foldertree02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TreeItem {

    private static final String FOLDER_SEPARATOR = "/";
    private static final String ROOT_FOLDER_NAME= "/";

    private String name;

    private List<TreeItem> children;

    private boolean readable;
    private boolean writable;

    public TreeItem(String name, List<TreeItem> children) {
        this.name = name;
        this.children = children;
    }

    public TreeItem(String name) {
        this.name = name;
        children = new ArrayList<>();
    }

    public void addChild(TreeItem newChild) {
        this.children.add(newChild);
    }

    public Optional<TreeItem> findChildByName(String name) {
        return children.stream().filter(child -> child.getName().equals(name)).findFirst();
    }

    /**
     * Used to build the tree.
     */
    public void addSubtree(String[] folders) {
        String childName = folders[0];
        final Optional<TreeItem> maybeChild = findChildByName(childName);
        TreeItem child;
        if (maybeChild.isPresent()) {
            child = maybeChild.get();
        } else {
            child = new TreeItem(childName);
            addChild(child);
        }
        final String[] remainingFolders = Arrays.copyOfRange(folders, 1, folders.length);
        if (remainingFolders.length > 0) {
            child.addSubtree(remainingFolders);
        }
    }

    /**
     * This sets up the readable info on the particular descendant folder denoted by the param. <br/>
     * Also checks for the data validity, ie. if there is a folder marked as readable which does not exist,
     * this method throws an IllegalStateException.
     */
    public void applyReadableSubtree(String[] folders) {
        String childName = folders[0];
        final Optional<TreeItem> maybeChild = findChildByName(childName);
        TreeItem child;
        if (maybeChild.isPresent()) {
            child = maybeChild.get();
            if (folders.length == 1) {
                child.setReadable(true);
            } else {
                final String[] remainingFolders = Arrays.copyOfRange(folders, 1, folders.length);
                child.applyReadableSubtree(remainingFolders);
            }
        } else {
            // should not happen if the data is correct
            throw new IllegalStateException("WARNING: A non-existent folder was marked as readable: " + this.name);
        }
    }

    /**
     * Removes the unreadable ones from the children.
     */
    public void purgeUnreadable() {
        children = children.stream()
                .filter(TreeItem::isReadable)
                .peek(TreeItem::purgeUnreadable)
                .collect(Collectors.toList());
    }

    /**
     * This sets up the writable info on the particular descendant folder denoted by the param. <br/>
     * Does not check for the data validity, ie. that the subfolder marked as writable actually exists,
     * those are simply skipped. This is so that this call works on a tree from which the unreadable subtrees have been purged.
     */
    public void applyWritableSubtree(String[] folders) {
        String childName = folders[0];
        final Optional<TreeItem> maybeChild = findChildByName(childName);
        TreeItem child;
        if (maybeChild.isPresent()) {
            child = maybeChild.get();
            if (folders.length == 1) {
                child.setWritable(true);
            } else {
                final String[] remainingFolders = Arrays.copyOfRange(folders, 1, folders.length);
                child.applyWritableSubtree(remainingFolders);
            }
        }
    }

    /**
     * A folder is needed if it is writable or if it has any writable descendant.
     * @return Returns whether this folder itself is still needed.
     */
    public boolean purgeUnneeded() {
        this.children = this.children.stream()
                .filter(TreeItem::purgeUnneeded)
                .collect(Collectors.toList());
        return this.isWritable() || this.children.size() > 0;
    }

    public List<String> print() {
        List<String> res = new ArrayList<>();
        final String thisLevel = this.name;
        res.add(thisLevel);
        for (TreeItem child : children) {
            for (String childPrint : child.print()) {
                res.add((thisLevel.equals(ROOT_FOLDER_NAME)
                    ? ""
                    : thisLevel) + FOLDER_SEPARATOR+ childPrint);
            }
        }
        return res;
    }

    public String getName() {
        return name;
    }

    public List<TreeItem> getChildren() {
        return children;
    }

    public boolean isReadable() {
        return readable;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public boolean isUnreadable() {
        return !this.readable;
    }

    @Override
    public String toString() {
        return "TreeItem [" +
                "name='" + name + '\'' +
                ", R=" + readable +
                ", W=" + writable +
                ", children=" + (children == null ? null : children.size()) +
                ']';
    }
}

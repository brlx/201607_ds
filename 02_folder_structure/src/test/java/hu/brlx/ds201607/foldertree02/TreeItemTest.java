package hu.brlx.ds201607.foldertree02;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeItemTest {

    private TreeItem cut;

    @Before
    public void before() {
        givenTI_Empty();
    }

    @Test
    public void testNewTreeItem_ShouldBeUnreadable() {
        givenTI_Empty();
        assertTrue("A new TreeItem should be unreadable", cut.isUnreadable());
    }

    @Test
    public void testCutUnreadable_Some() {
        givenTI_Readable_WithReadableAndUnreadableChildren();
        cut.purgeUnreadable();
        assertEquals("Unreadable children should be purged", cut.getChildren().size(), 3);
    }

    @Test
    public void testPurgeUnreadable_None() {
        givenTI_Readable_WithReadableOnlyChildren();
        final int childrenCount = cut.getChildren().size();
        cut.purgeUnreadable();
        assertEquals("No readable children should be purged with purgeUnreadable()", cut.getChildren().size(), childrenCount);
    }

    @Test
    public void testPurgeUnreadable_All() {
        givenTI_Unreadable_WithUnreadableChildren();
        cut.purgeUnreadable();
        assertEquals("All readable children should be purged", cut.getChildren().size(), 0);
    }

    @Test
    public void testPurgeUnneeded_Writable_NoPurge() {
        givenTI_Writable_WithWritableChildren();
        final int childCount = cut.getChildren().size();
        final boolean stillNeeded = cut.purgeUnneeded();
        assertTrue("A writable folder with writable children should not be reported as still needed", stillNeeded);
        assertEquals("No writable children should be purged", childCount, cut.getChildren().size());
    }

    @Test
    public void testPurgeUnneeded_Writable_SomePurge() {
        givenTI_Writable_withVariousChildren();
        final int childCount = cut.getChildren().size();
        final boolean stillNeeded = cut.purgeUnneeded();
        assertTrue("A writable TreeItem with some writable children should be reported as unneeded", stillNeeded);
        assertEquals("The readonly children should be purged", childCount - 1, cut.getChildren().size());
    }

    @Test
    public void testPurgeUnneeded_Writable_PurgeAllChildren() {
        givenTI_Writable_WithReadonlyChildren();
        final boolean stillNeeded = cut.purgeUnneeded();
        assertTrue("A writable folder should not be reported as still needed even if none of its children are needed", stillNeeded);
        assertEquals("All of the children must be purged if they are unneeded", 0, cut.getChildren().size());
    }

    @Test
    public void testPurgeUnneeded_Readable_NoPurge() {
        givenTI_Readonly_WithWritableChildren();
        final int childCount = cut.getChildren().size();
        final boolean stillNeeded = cut.purgeUnneeded();
        assertTrue("A readonly folder should be still needed if it has writable children", stillNeeded);
        assertEquals("None of the writable children should be purged even if the folder is readonly", childCount, cut.getChildren().size());
    }

    @Test
    public void testPurgeUnneeded_Readable_SomePurge() {
        givenTI_Readonly_withVariousChildren();
        final int childCount = cut.getChildren().size();
        final boolean stillNeeded = cut.purgeUnneeded();
        assertTrue("A readonly folder should be still needed if it has writable children", stillNeeded);
        assertEquals("None of the writable children should be purged even if the folder is readonly", childCount - 1, cut.getChildren().size());
    }

    @Test
    public void testPurgeUnneeded_Readable_PurgeAllChildren() {
        givenTI_Readonly_WithReadonlyChildren();
        final boolean stillNeeded = cut.purgeUnneeded();
        assertFalse("A readonly folder should not be needed if it has only readonly children", stillNeeded);
        assertEquals("All of the readonly children should be purged if the folder is readonly", 0, cut.getChildren().size());
    }

    private void givenTI_Empty() {
        cut = new TreeItem("test");
    }

    private void givenTI_Unreadable_WithUnreadableChildren() {
        givenTI_Empty();
        cut.addChild(new TreeItem("unreadable_1"));
        cut.addChild(new TreeItem("unreadable_2"));
        cut.addChild(new TreeItem("unreadable_3"));
    }

    private void givenTI_Readable_WithReadableOnlyChildren() {
        givenTI_Empty();
        cut.setReadable(true);
        for (int i = 0; i < 3; i++) {
            final TreeItem newChild = new TreeItem("readable_" + i);
            newChild.setReadable(true);
            cut.addChild(newChild);
        }
    }

    private void givenTI_Readable_WithReadableAndUnreadableChildren() {
        givenTI_Readable_WithReadableOnlyChildren();
        cut.addChild(new TreeItem("unReadable_1"));
    }


    private void givenTI_Writable_WithWritableChildren() {
        givenTI_Empty();
        cut.setReadable(true);
        cut.setWritable(true);
        for (int i = 0; i < 3; i++) {
            TreeItem newChild = new TreeItem("writableChild_" + i);
            newChild.setReadable(true);
            newChild.setWritable(true);
            cut.addChild(newChild);
        }
    }

    private void givenTI_Writable_withVariousChildren() {
        givenTI_Writable_WithWritableChildren();
        TreeItem newChild = new TreeItem("readonlyChild_1");
        cut.addChild(newChild);
        newChild.setReadable(true);
    }

    private void givenTI_Writable_WithReadonlyChildren() {
        givenTI_Empty();
        cut.setReadable(true);
        cut.setWritable(true);
        for (int i = 0; i < 3; i++) {
            TreeItem newChild = new TreeItem("writableChild_" + i);
            newChild.setReadable(true);
            cut.addChild(newChild);
        }
    }

    private void givenTI_Readonly_WithWritableChildren() {
        givenTI_Empty();
        cut.setReadable(true);
        for (int i = 0; i < 3; i++) {
            TreeItem newChild = new TreeItem("writableChild_" + i);
            newChild.setReadable(true);
            newChild.setWritable(true);
            cut.addChild(newChild);
        }
    }

    private void givenTI_Readonly_withVariousChildren() {
        givenTI_Readonly_WithWritableChildren();
        TreeItem newChild = new TreeItem("readonlyChild_1");
        newChild.setReadable(true);
        cut.addChild(newChild);
    }

    private void givenTI_Readonly_WithReadonlyChildren() {
        givenTI_Empty();
        cut.setReadable(true);
        for (int i = 0; i < 3; i++) {
            TreeItem newChild = new TreeItem("writableChild_" + i);
            newChild.setReadable(true);
            cut.addChild(newChild);
        }
    }

}

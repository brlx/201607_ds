# Solution for the folder structure challenge

This module contains the solution for the folder structure challenge.

## Usage

The provided solution can be run from the integration test (`TreeFiltererIT`), where the test provides the class with initial folder data, parsed from an input file, and also with the actual input (readable and writable folders).

## How it works

The solution (`TreeFilterer`) takes the complete folder structure as provided.

Its other two inputs are the readable paths and the writable paths, both as a `String[]`.

The actual algorithm is implemented by the `TreeItem#purgeUnreadable` and the `TreeItem#purgeUnneeded` methods, both recursively. But first, `TreeFilterer` takes the readable paths, applies them to the tree with `TreeItem#applyReadableSubtree`.
Right after this, `TreeItem#purgeUnreadable` removes the subfolders which are not reachable from via readable subfolders. This is done searching tree breadth-first, since the availability of the folders does not depend on their children, it only depends on their own readability. This way, the unreachable subfolders are dropped ASAP.
Next, the `TreeItem#applyWritableSubtree` method applies the writability info, but now it does not have to care about the unreachable parts.
Next, the `TreeItem#purgeUnneeded` purges the folders which are not needed. A folder is needed if it is writable itself or it has a descendant folder which is writable. So since this property depends on the children of the folder, deciding the usefulness requires a depth-first search.

## Possible future improvements

* It should be possible to enable/disable the purging of the unreadable folders (`TreeItem#purgeUnreadable`) before applying the writable folders. This would be useful, if we know that most of the complete folder structure is readable so that it does not make a big performance gain if we remove the unreadable parts in a separate operation. But right now, `TreeItem#purgeUnreadable` and `TreeItem#purgeUnneeded` has both to be run, in this order.



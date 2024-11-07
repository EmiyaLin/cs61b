package gitlet;

import jdk.jshell.execution.Util;

import javax.management.remote.JMXServerErrorException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class does at a high level.
 *
 * @author Xinran Zhao
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    public static final File GITLET_COMMIT = join(GITLET_DIR, "commit");

    public static final File GITLET_BLOB = join(GITLET_DIR, "blob");

    public static final File GITLET_STAGINGAREA = join(GITLET_DIR, "stagingArea");

    public static final File HEAD = join(GITLET_DIR, "HEAD");

    public static final File BRANCH = join(GITLET_DIR, "branch");

    public static final File MASTER = join(BRANCH, "master");

    public static String master;

//    private static Set<String> stagingFilesSet = new HashSet<>();

    /* TODO: fill in the rest of this class. */

    /**
     * Creates a new Gitlet version-control system in the current directory. This system will automatically start with one commit: a commit that contains no files and has the commit message initial commit (just like that, with no punctuation). It will have a single branch: master, which initially points to this initial commit, and master will be the current branch. The timestamp for this initial commit will be 00:00:00 UTC, Thursday, 1 January 1970 in whatever format you choose for dates (this is called “The (Unix) Epoch”, represented internally by the time 0.) Since the initial commit in all repositories created by Gitlet will have exactly the same content, it follows that all repositories will automatically share this commit (they will all have the same UID) and all commits in all repositories will trace back to it.
     */
    public static void init() throws IOException {
        GITLET_DIR.mkdir();
        Commit initialCommit = new Commit();
        master = initialCommit.getUID();
        GITLET_COMMIT.mkdir();
        GITLET_BLOB.mkdir();
        GITLET_STAGINGAREA.mkdir();
        BRANCH.mkdir();
        HEAD.createNewFile();
        MASTER.createNewFile();
        Utils.writeObject(HEAD, initialCommit.getUID());
        Utils.writeObject(MASTER, initialCommit.getUID());
        File commit = Utils.join(GITLET_COMMIT, initialCommit.getUID());
        commit.createNewFile();
        Utils.writeObject(commit, initialCommit);
//        System.out.println(readObject(commit, Commit.class).toString());
    }

    /**
     * Description: Adds a copy of the file as it currently exists to the staging area (see the description of the commit command).
     * For this reason, adding a file is also called staging the file for addition.
     * Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * The staging area should be somewhere in .gitlet.
     * If the current working version of the file is identical to the version in the current commit, do not stage it to be added,
     * and remove it from the staging area if it is already there (as can happen when a file is changed, added, and then changed back to it’s original version).
     * The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     */
    /**
     * 要把stagingFile 存入 缓存区
     * 1. If the current working version of the file is identical to the version in the current commit, do not stage it to be added,
     *    and remove it from the staging area if it is already there
     * 1. 先判断一下currentCommit里有没有这个文件啊
     */
    public static void add(String filename) throws IOException {
        String fileContent = Utils.readContentsAsString(join(CWD, filename));
        System.out.println("original file content: " + fileContent);
        String uid = Utils.sha1(fileContent);
        File stagingFile = Utils.join(GITLET_STAGINGAREA, filename);
        List<String> stagingFilesList = Utils.plainFilenamesIn(GITLET_STAGINGAREA);
        if (getCurrentCommit().getTrackingFile().containsKey(filename)) {
            if (stagingFilesList.contains(filename)) {
                stagingFilesList.remove(filename);
                stagingFile.delete();
            }
        } else {
            if (!stagingFilesList.contains(filename)) {
                stagingFile.createNewFile();
                Utils.writeContents(stagingFile, fileContent);
            } else {
                System.out.println("read from file: " + readContentsAsString(stagingFile));
                Utils.writeContents(stagingFile, fileContent);
            }
            String content = Utils.readContentsAsString(stagingFile);
            System.out.println("write in file: " + content);
        }
    }

    private static Commit getCurrentCommit() {
        String currentCommitUid = Utils.readObject(HEAD, String.class);
        return Utils.readObject(join(GITLET_COMMIT, currentCommitUid), Commit.class);
    }

    /**
     * Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time,
     * creating a new commit. The commit is said to be tracking the saved files.
     * By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     * A commit will only update the contents of files it is tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file that was staged instead of the version it got from its parent.
     * A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
     * Finally, files tracked in the current commit may be untracked in the new commit as a result being staged for removal by the rm command (below).
     */
    public static void commit(String message) throws IOException {
        Commit normalCommit = new Commit(message, new Date(), getCurrentCommit().getUID(),
                getCurrentCommit().getTrackingFile(), plainFilenamesIn(GITLET_STAGINGAREA));
        File commitFile = Utils.join(GITLET_COMMIT, normalCommit.getUID());
        commitFile.createNewFile();
        Utils.writeObject(commitFile, normalCommit);
        Utils.writeContents(HEAD, normalCommit.getUID());
        Utils.writeContents(MASTER, normalCommit.getUID());
        clearStagingArea();
    }

    private static void clearStagingArea() {
        List<String> stagingFiles = Utils.plainFilenamesIn(GITLET_STAGINGAREA);
        if (stagingFiles == null) {
            return;
        }
        for (String filename : stagingFiles) {
            join(GITLET_STAGINGAREA, filename).delete();
        }
    }
}

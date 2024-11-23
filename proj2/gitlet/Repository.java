package gitlet;

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

    public static final File GITLET_REMOVALAREA = join(GITLET_DIR, "removalArea");

    public static final File HEAD = join(GITLET_DIR, "HEAD");

    public static final File BRANCH = join(GITLET_DIR, "branch");

    public static final File MASTER = join(BRANCH, "master");

    public static final File INITIALCOMMIT = join(GITLET_DIR, "initialCommit");

    public static final File CURRENT_BRANCH = join(GITLET_DIR, "current_branch");

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
        GITLET_REMOVALAREA.mkdir();
        BRANCH.mkdir();
        HEAD.createNewFile();
        MASTER.createNewFile();
        INITIALCOMMIT.createNewFile();
        CURRENT_BRANCH.createNewFile();
        Utils.writeContents(HEAD, initialCommit.getUID());
        Utils.writeContents(MASTER, initialCommit.getUID());
        Utils.writeContents(INITIALCOMMIT, initialCommit.getUID());
        Utils.writeContents(CURRENT_BRANCH, initialCommit.getBranch());
        File commit = Utils.join(GITLET_COMMIT, initialCommit.getUID());
        commit.createNewFile();
        Utils.writeObject(commit, initialCommit);
    }

    /**
     * Description: Adds a copy of the file as it currently exists to the staging area
     * (see the description of the commit command).
     * For this reason, adding a file is also called staging the file for addition.
     *
     * Staging an already-staged file overwrites the previous entry in the staging area with the new contents.
     * The staging area should be somewhere in .gitlet.
     * If the current working version of the file is identical to the version in the current commit, do not stage
     * it to be added,
     * and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back to it’s original version).
     * The file will no longer be staged for removal (see gitlet rm), if it was at the time of the command.
     */
    /**
     * 要把stagingFile 存入 缓存区
     * 1. If the current working version of the file is identical to the version in the current commit, do not stage it to be added,
     * and remove it from the staging area if it is already there
     * 1. 先判断一下currentCommit里有没有这个文件啊
     */
    public static void add(String filename) throws IOException {
        String fileContent = Utils.readContentsAsString(join(CWD, filename));
        String uid = Utils.sha1(fileContent);
        File stagingFile = Utils.join(GITLET_STAGINGAREA, filename);
        List<String> stagingFilesList = new ArrayList<>(Utils.plainFilenamesIn(GITLET_STAGINGAREA)); // 暂存区
        List<String> removalFileList = Utils.plainFilenamesIn(GITLET_REMOVALAREA);
        if (removalFileList.contains(filename)) {
            join(GITLET_REMOVALAREA, filename).delete();
        }
        Commit currentCommit = getCurrentCommit();
        if (currentCommit.getTrackingFile().containsKey(filename)) { // 要stage的文件被跟踪
            if (checkIdentical(uid, filename)) {  // 判断内容和blob相同
                if (stagingFilesList.contains(filename)) { // 判断暂存区内有
                    stagingFile.delete();
                    stagingFilesList.remove(filename);
                }
                return;
            } else {
                Utils.writeContents(join(GITLET_STAGINGAREA, filename), fileContent);
            }
        } else {
            if (!stagingFilesList.contains(filename)) {
                stagingFile.createNewFile();
                Utils.writeContents(stagingFile, fileContent);
            } else {
                Utils.writeContents(stagingFile, fileContent);
            }
        }
    }

    /**
     判断当前commit里跟踪的blob文件和要stage的文件内容是否相同
     * @param uid 要stage的文件内容的uid
     * @param filename 要stage的文件名
     * @return
     */
    private static boolean checkIdentical(String uid, String filename) {
        return uid.equals(getCurrentCommit().getTrackingFile().get(filename));
    }

    public static Commit getCurrentCommit() {
        String currentCommitUid = Utils.readContentsAsString(HEAD);
        return Utils.readObject(join(GITLET_COMMIT, currentCommitUid), Commit.class);
    }

    /**
     * Saves a snapshot of tracked files in the current commit and staging area so they can be restored at a later time,
     * creating a new commit. The commit is said to be tracking the saved files.
     * By default, each commit’s snapshot of files will be exactly the same as its parent commit’s snapshot of files;
     * it will keep versions of files exactly as they are, and not update them.
     * A commit will only update the contents of files it is
     * tracking that have been staged for addition at the time of commit,
     * in which case the commit will now include the version of the file
     * that was staged instead of the version it got from its parent.
     * A commit will save and start tracking any files that were staged for addition but weren’t tracked by its parent.
     * Finally, files tracked in the current commit may be untracked in the new commit
     * as a result being staged for removal by the rm command (below).
     */
    public static void commit(String message) throws IOException {
        String currentBranch = Utils.readContentsAsString(join(GITLET_DIR, "current_branch"));
        List<String> stagingArea = Utils.plainFilenamesIn(GITLET_STAGINGAREA);
        List<String> removalArea = Utils.plainFilenamesIn(GITLET_REMOVALAREA);
        if (stagingArea.isEmpty() && removalArea.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        Commit normalCommit = new Commit(message, new Date(), getCurrentCommit().getUID(),
                getCurrentCommit().getTrackingFile(), plainFilenamesIn(GITLET_STAGINGAREA),
                plainFilenamesIn(GITLET_REMOVALAREA), currentBranch);
        File commitFile = Utils.join(GITLET_COMMIT, normalCommit.getUID());
        commitFile.createNewFile();
        Utils.writeObject(commitFile, normalCommit);
        Utils.writeContents(HEAD, normalCommit.getUID());
        File branch = join(BRANCH, currentBranch);
        Utils.writeContents(branch, normalCommit.getUID());
        clearStagingArea();
        clearRemovalArea();
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

   private static void clearRemovalArea() {
        List<String> removalFiles = Utils.plainFilenamesIn(GITLET_REMOVALAREA);
        if (removalFiles == null) {
            return;
        }
        for (String filename : removalFiles) {
            join(GITLET_REMOVALAREA, filename).delete();
        }
   }

    /**
     * Unstage the file if it is currently staged for addition. If the file is tracked in the current commit, stage
     * it for removal
     * and remove the file from the working directory if the user has not already done so (do not remove it unless
     * it is tracked in the current commit).
     * Failure cases: If the file is neither staged nor tracked by the head commit,
     * print the error message No reason to remove the file.
     * @param filename
     */
    public static void rm(String filename) throws IOException {
        Commit currentCommit = getCurrentCommit();
        List<String> stagingFilesList = new ArrayList<>(Utils.plainFilenamesIn(GITLET_STAGINGAREA));
        Map<String, String> trackingFiles = currentCommit.getTrackingFile();
        if (!stagingFilesList.contains(filename) && !currentCommit.getTrackingFile().containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        if (stagingFilesList.contains(filename)) {
            join(GITLET_STAGINGAREA, filename).delete();
        }
        if (currentCommit.getTrackingFile().containsKey(filename)) {
            join(GITLET_REMOVALAREA, filename).createNewFile();
            Utils.writeContents(join(GITLET_REMOVALAREA, filename), currentCommit.getTrackingFile().get(filename));
            if (join(CWD, filename).exists()) {
                Utils.restrictedDelete(join(CWD, filename));
            }
        }
    }

    // Starting at the current head commit, display information about each commit backwards along the commit tree
    // until the initial commit, following the first parent commit links, ignoring any second parents found in merge
    // commits. (In regular Git, this is what you get with git log --first-parent). This set of commit nodes is
    // called the commit’s history. For every node in this history, the information it should display is the commit
    // id, the time the commit was made, and the commit message.
    public static void log() {
        Commit currentCommit = getCurrentCommit();
        String initialCommitUid = Utils.readContentsAsString(INITIALCOMMIT);
        while (!currentCommit.getUID().equals(initialCommitUid)) {
            showCommitInfo(currentCommit);
            String parentUid = currentCommit.getParent();
            currentCommit = Utils.readObject(join(GITLET_COMMIT, parentUid),Commit.class);
        }
        showCommitInfo(currentCommit);
    }

    private static void showCommitInfo(Commit currentCommit) {
        Formatter fomatter = new Formatter();
        System.out.println("===");
        System.out.println("commit " + currentCommit.getUID());
        fomatter.format("%1$ta %1$tb %1$td %1$tT %1$tY %1$tz", currentCommit.getTimestamp());
        System.out.println("Date: " + fomatter);
        System.out.println(currentCommit.getMessage());
        System.out.println();
    }

    private static Commit getCommit(String commitUid) {
        return Utils.readObject(join(GITLET_COMMIT, commitUid), Commit.class);
    }

    public static void globalLog() {
        List<String> commitUidList = Utils.plainFilenamesIn(GITLET_COMMIT);
        for (String commitUid : commitUidList) {
            showCommitInfo(getCommit(commitUid));
        }
    }

    /**
     * Description: Prints out the ids of all
     * commits that have the given commit message,
     * one per line. If there are multiple such commits,
     * it prints the ids out on separate lines.
     * The commit message is a single operand;
     * to indicate a multiword message, put the operand in quotation marks,
     * as for the commit command below. Hint: the hint for this command is the same as the one for global-log.
     * @param message
     */
    public static void find(String message) {
        List<String> commitUidList = Utils.plainFilenamesIn(GITLET_COMMIT);
        boolean findFlag = false;
        for (String commitUid : commitUidList) {
            Commit commit = getCommit(commitUid);
            String commitMessage = commit.getMessage();
            if (commitMessage.equals(message)) {
                findFlag = true;
                System.out.println(commit.getUID());
            }
        }
        if (!findFlag) {
            System.out.println("Found no commit with that message.");
        }
    }

    /**
     * Takes the version of the file as it exists in the head commit and puts it
     * in the working directory, overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     * @param filename
     */
    public static void checkout(String filename) {
        Commit currentCommit = getCurrentCommit();
        checkoutFile(filename, currentCommit);
    }

    /**
     * Takes the version of the file as it exists in the commit with the given id, and puts it in the working
     * directory, overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.
     * @param commitUid
     * @param filename
     */
    public static void checkout(String commitUid, String filename) {
        if (!join(GITLET_COMMIT, commitUid).exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit commit = getCommit(commitUid);
        checkoutFile(filename, commit);
    }

    private static void checkoutFile(String filename, Commit commit) {
        if (!commit.getTrackingFile().containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        String trackingFileUid = commit.getTrackingFile().get(filename);
        String checkoutContent = Utils.readContentsAsString(join(GITLET_BLOB, trackingFileUid));
        Utils.writeContents(join(CWD, filename), checkoutContent);
        if (join(GITLET_STAGINGAREA, filename).isFile()) {
            join(GITLET_STAGINGAREA, filename).delete();
        }
        if (join(GITLET_REMOVALAREA, filename).isFile()) {
            join(GITLET_REMOVALAREA, filename).delete();
        }
    }

    /**
     * Takes all files in the commit at the head of the given branch, and puts them in the working directory,
     * overwriting the versions of the files that are already there if they exist.
     * Also, at the end of this command, the given branch will now be considered the current branch (HEAD).
     * Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
     * The staging area is cleared, unless the checked-out branch is the current branch (see Failure cases below).
     * @param branch
     */
    public static void checkoutBranch(String branch) {
        List<String> branches = Utils.plainFilenamesIn(BRANCH);
        if (!join(BRANCH, branch).exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        Commit currentCommit = getCurrentCommit();
        String commitUid = Utils.readContentsAsString(join(BRANCH, branch));
        Commit commit = getCommit(commitUid);
        Map<String, String> checkoutTrackingFile = commit.getTrackingFile();
        Set<String> checkoutFileNames = checkoutTrackingFile.keySet();
        Map<String, String> currentTrackingFile = currentCommit.getTrackingFile();
        Set<String> currentTrackingFileNames = currentTrackingFile.keySet();
        if (!branches.contains(branch)) {
            System.out.println("No such branch exists");
            System.exit(0);
        } else if (branch.equals(Utils.readContentsAsString(CURRENT_BRANCH))) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }  else if (untrackedFileCheck(checkoutTrackingFile, currentTrackingFile)) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        } else {
            for (String checkoutFileName : checkoutFileNames) {
                String checkoutFileUid = checkoutTrackingFile.get(checkoutFileName);
                String checkoutContent = Utils.readContentsAsString(join(GITLET_BLOB, checkoutFileUid));
                Utils.writeContents(join(CWD, checkoutFileName), checkoutContent);
            }
            for (String currentTrackingFileName : currentTrackingFile.keySet()) {
                if (!checkoutTrackingFile.containsKey(currentTrackingFileName)) {
                    join(CWD, currentTrackingFileName).delete();
                }
            }
            Utils.writeContents(CURRENT_BRANCH, branch);
            Utils.writeContents(HEAD, commitUid);
            clearStagingArea();
            clearRemovalArea();
        }
    }

    //  If a working file is untracked in the current branch and would be overwritten by the checkout
    private static boolean untrackedFileCheck(Map<String, String> checkoutTrackingFile,
                                              Map<String, String> currentTrackingFile) {
        Set<String> checkoutTrackingFileNames = checkoutTrackingFile.keySet();
        for (String checkoutTrackingFileName : checkoutTrackingFileNames) {
            if (join(CWD, checkoutTrackingFileName).exists() && !currentTrackingFile.containsKey(checkoutTrackingFileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new branch with the given name, and points it at the current head commit.
     * A branch is nothing more than a name for a reference (a SHA-1 identifier) to a commit node.
     * This command does NOT immediately switch to the newly created branch (just as in real Git).
     * Before you ever call branch, your code should be running with a default branch called “master”.
     * @param branch
     */
    public static void createBranch(String branch) {
        List<String> existingBranch = Utils.plainFilenamesIn(BRANCH);
        if (existingBranch.contains(branch)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Commit currentCommit = getCurrentCommit();
        String currentCommitUid = currentCommit.getUID();
        Utils.writeContents(join(BRANCH, branch), currentCommitUid);
    }

    /**
     *  Displays what branches currently exist,
     *  and marks the current branch with a *.
     *  Also displays what files have been staged for addition or removal.
     *  An example of the exact format it should follow is as follows.
     * <p>
     *  === Branches ===
     * *master
     * other-branch
     * <p>
     * === Staged Files ===
     * wug.txt
     * wug2.txt
     * <p>
     * === Removed Files ===
     * goodbye.txt
     * <p>
     * === Modifications Not Staged For Commit ===
     * junk.txt (deleted)
     * wug3.txt (modified)
     * <p>
     * === Untracked Files ===
     * random.stuff
     */
    public static void showStatus() {
        Commit currentCommit = getCurrentCommit();
        String currentBranch = Utils.readContentsAsString(CURRENT_BRANCH);
        List<String> branchList = Utils.plainFilenamesIn(BRANCH);
        branchList.sort(String::compareTo);
        System.out.println("=== Branches ===");
        for (String branch : branchList) {
            if (!branch.equals(currentBranch)) {
                System.out.println(branch);
            } else {
                System.out.println("*" + branch);
            }
        }
        System.out.println();
        List<String> stagingFileList = Utils.plainFilenamesIn(GITLET_STAGINGAREA);
        stagingFileList.sort(String::compareTo);
        System.out.println("=== Staged Files ===");
        for (String stagedFile : stagingFileList) {
            System.out.println(stagedFile);
        }
        System.out.println();
        List<String> removedFileList = Utils.plainFilenamesIn(GITLET_REMOVALAREA);
        removedFileList.sort(String::compareTo);
        System.out.println("=== Removed Files ===");
        for (String removedFile : removedFileList) {
            System.out.println(removedFile);
        }
        System.out.println();
        //  A file in the working directory is “modified but not staged” if it is
        //
        //  Tracked in the current commit, changed in the working directory, but not staged; or
        //  Staged for addition, but with different contents than in the working directory; or
        //  Staged for addition, but deleted in the working directory; or
        //  Not staged for removal, but tracked in the current commit and deleted from the working directory.
        List<String> lexicographicPrintList = new ArrayList<>();
        Map<String, String> currentCommitTrackedFile = currentCommit.getTrackingFile();
        List<String> currentCommitTrakcedFileNameList = new ArrayList<>(currentCommitTrackedFile.keySet());
        currentCommitTrakcedFileNameList.sort(String::compareTo);
        for (String currentCommitTrackedFileName : currentCommitTrakcedFileNameList) {
            String currentCommitTrackedFileUid = currentCommitTrackedFile.get(currentCommitTrackedFileName);
            List<String> fileInCWD = Utils.plainFilenamesIn(CWD);
            if (statusCheck1(currentCommitTrackedFileUid, currentCommitTrackedFileName, fileInCWD, stagingFileList)) {
                lexicographicPrintList.add(currentCommitTrackedFileName + " (modified)");
            } else if (statusCheck4(currentCommitTrackedFileName, removedFileList)) {
                lexicographicPrintList.add(currentCommitTrackedFileName + " (deleted)");
            }
        }
        for (String stagedFileName : stagingFileList) {
            if (!statusCheck2(stagedFileName, Utils.plainFilenamesIn(CWD))) {
                lexicographicPrintList.add(stagedFileName + " (modified)");
            } else if (statusCheck3(stagedFileName, Utils.plainFilenamesIn(CWD))) {
                lexicographicPrintList.add(stagedFileName + " (deleted)");
            }
        }
        lexicographicPrintList.sort(String::compareTo);
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String str : lexicographicPrintList) {
            System.out.println(str);
        }
        System.out.println();
        // The final category (“Untracked Files”) is for files present in the working directory
        // but neither staged for addition nor tracked.
        // This includes files that have been staged for removal, but then re-created without Gitlet’s knowledge.
        // Ignore any subdirectories that may have been introduced, since Gitlet does not deal with them.
        List<String> workingDirectory = Utils.plainFilenamesIn(CWD);
        System.out.println("=== Untracked Files ===");
        for (String fileNameInWorkingDirectory : workingDirectory) {
            if (!stagingFileList.contains(fileNameInWorkingDirectory) &&
                    !currentCommitTrackedFile.containsKey(fileNameInWorkingDirectory)) {
                System.out.println(fileNameInWorkingDirectory);
            }
        }
    }

    // Tracked in the current commit, changed in the working directory, but not staged;
    private static boolean statusCheck1(String currentCommitTrackedFileUid, String currentCommitTrackedFileName,
                                        List<String> fileInCWD, List<String> stagingFielList) {
        if (fileInCWD.contains(currentCommitTrackedFileName)) {
            File CWDFile = join(CWD, currentCommitTrackedFileName);
            String CWDFileUid =  sha1(Utils.readContentsAsString(CWDFile));
            if (!CWDFileUid.equals(currentCommitTrackedFileUid)) {
                if (!stagingFielList.contains(currentCommitTrackedFileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Staged for addition, but with different contents than in the working directory;
    private static boolean statusCheck2(String stagedFileName, List<String> fileInCWD) {
        if (!fileInCWD.contains(stagedFileName)) {
            return false;
        }
        String stagedFileUid = sha1(Utils.readContentsAsString(join(GITLET_STAGINGAREA, stagedFileName)));
        String fileInCWDUid = sha1(Utils.readContentsAsString(join(CWD, stagedFileName)));
        return stagedFileUid.equals(fileInCWDUid);
    }

    // Staged for addition, but deleted in the working directory;
    private static boolean statusCheck3(String stagedFileName, List<String> fileInCWD) {
       return !fileInCWD.contains(stagedFileName);
    }

    // Not staged for removal, but tracked in the current commit and deleted from the working directory
    private static boolean statusCheck4(String currentCommitTrackedFileName, List<String> removedFileList) {
        return !removedFileList.contains(currentCommitTrackedFileName) &&
                !join(CWD, currentCommitTrackedFileName).exists();
    }
}

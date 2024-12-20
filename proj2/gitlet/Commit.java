package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *  Commit could be a Map with FileName as key version as value
 *  it has message, timestamp, and keeps track of the file which be modified
 *  it also has a pointer points to the parent commit.
 *  branch
 *
 *  @author Xinran Zhao
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;


    /** The timestamp of this Commit. */
    private Date timestamp;

    // The pointer points to the parent commit.
    private String parent;

    private String secondParent;

    // file 存 Commit相关的Blob（uid) <Hello.txt, uid>
    private Map<String, String> trackingFile;

    private String UID;

    private String branch;

    /**
     *  create a commit that contains no files and has
     *  the commit message initial commit (just like that, with no
     *  punctuation). It will have a single branch: master,
     *  which initially points to this initial commit, and master
     *  will be the current branch.(not concluded in this method)
     *  The timestamp for this initial commit will be
     *  00:00:00 UTC,
     *  Thursday, 1
     *  January 1970
     *  in whatever format you choose for dates (this is called “The (Unix) Epoch”,
     *  represented internally by the time 0.)
     *  Since the initial commit in all repositories created
     *  by Gitlet will have exactly the same content, it follows
     *  that all repositories will automatically share
     *  this commit (they will all have the same UID) and all commits in
     *  all repositories will trace back to it.
     */
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parent = null;
        this.secondParent = null;
        this.UID = Utils.sha1(message, timestamp.toString());
        this.trackingFile = new HashMap<>();
        this.branch = "master";
    }

    /**
     * 如果暂存区里的文件Commit里没有跟踪，那么就添加到Commit里
     * 如果正在跟踪，那么只需要modify
     * If the file is tracked in the current commit,
     * stage it for removal and remove the file from the working
     * directory if the user has not already done so
     * (do not remove it unless it is tracked in the current commit)
     * @param message
     * @param timestamp
     * @param parent
     * @param trackingFile
     * @param stagingFilesList
     */
    public Commit(String message, Date timestamp, String parent, Map<String, String> trackingFile,
                  List<String> stagingFilesList, List<String> removalFilesList, String branch) {
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        this.branch = branch;
        this.secondParent = null;
        for (String removalFile : removalFilesList) {
            trackingFile.remove(removalFile);
        }
        for (String filename : stagingFilesList) {
            File stagingFile = Utils.join(Repository.GITLET_STAGINGAREA, filename);
            String stagingFileUid = Utils.sha1(Utils.readContentsAsString(stagingFile));
            // 没有跟踪
            if (!trackingFile.containsKey(filename)) {
                trackingFile.put(filename, stagingFileUid);
            } else {
                trackingFile.remove(filename);
                trackingFile.put(filename, stagingFileUid);
            }
            Utils.writeContents(Utils.join(Repository.GITLET_BLOB, stagingFileUid),
                    Utils.readContentsAsString(Utils.join(Repository.GITLET_STAGINGAREA,
                            filename)));
        }
        this.trackingFile = trackingFile;
        this.UID = Utils.sha1(message, timestamp.toString(), parent, trackingFile.toString());
    }

    public Commit(String message, Date timestamp, String parent, Map<String, String> trackingFile,
                  List<String> stagingFilesList, List<String> removalFilesList, String branch,
                  String secondParentUid) {
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        this.branch = branch;
        this.secondParent = secondParentUid;
        for (String removalFile : removalFilesList) {
            trackingFile.remove(removalFile);
        }
        for (String filename : stagingFilesList) {
            File stagingFile = Utils.join(Repository.GITLET_STAGINGAREA, filename);
            String stagingFileUid = Utils.sha1(Utils.readContentsAsString(stagingFile));
            // 没有跟踪
            if (!trackingFile.containsKey(filename)) {
                trackingFile.put(filename, stagingFileUid);
            } else {
                trackingFile.remove(filename);
                trackingFile.put(filename, stagingFileUid);
            }
            Utils.writeContents(Utils.join(Repository.GITLET_BLOB, stagingFileUid),
                    Utils.readContentsAsString(Utils.join(Repository.GITLET_STAGINGAREA,
                            filename)));
        }
        this.trackingFile = trackingFile;
        this.UID = Utils.sha1(message, timestamp.toString(), parent, trackingFile.toString());
    }

    public void setParent(String uid) {
        this.parent = uid;
    }

    public String getUID() {
        return UID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return this.message;
    }
    public String getParent() {
        return parent;
    }
    public String getBranch() {
        return branch;
    }
    public String getSecondParent() {
        return secondParent;
    }
    public Map<String, String> getTrackingFile() {
        return trackingFile;
    }

    public String toString() {
        return this.message + ' ' + this.timestamp + ' ' + this.UID + " parent " + this.parent;
    }
}

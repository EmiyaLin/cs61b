package gitlet;

// TODO: any imports you need here


import java.io.File;
import java.io.Serializable;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
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
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /* TODO: fill in the rest of this class. */

    /** The timestamp of this Commit. */
    private Date timestamp;

    // The pointer points to the parent commit.
    private String parent;

    // file 存 Commit相关的Blob（uid) <Hello.txt, uid>
    private Map<String, String> trackingFile;

    // TODO: UID to be finished, may be use
    private String UID;

    /**
     *  create a commit that contains no files and has the commit message initial commit (just like that, with no
     *  punctuation). It will have a single branch: master, which initially points to this initial commit, and master
     *  will be the current branch.(not concluded in this method) The timestamp for this initial commit will be
     *  00:00:00 UTC,
     *  Thursday, 1
     *  January 1970
     *  in whatever format you choose for dates (this is called “The (Unix) Epoch”, represented internally by the time 0.)
     *  Since the initial commit in all repositories created by Gitlet will have exactly the same content, it follows
     *  that all repositories will automatically share this commit (they will all have the same UID) and all commits in
     *  all repositories will trace back to it.
     */
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.parent = null;
        this.UID = Utils.sha1(message, timestamp.toString());
        this.trackingFile = new HashMap<>();
//        System.out.println(timestamp.toString());
//        System.out.println(UID);
    }

    /**
     * 如果暂存区里的文件Commit里没有跟踪，那么就添加到Commit里
     * 如果正在跟踪，那么只需要modify
     * @param message
     * @param timestamp
     * @param parent
     * @param trackingFile
     * @param stagingFilesList
     */
    public Commit(String message, Date timestamp, String parent, Map<String, String> trackingFile ,
                  List<String> stagingFilesList) {
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        this.trackingFile = trackingFile;
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
                    Utils.readContentsAsString(Utils.join(Repository.GITLET_STAGINGAREA, filename)));
        }
        this.UID = Utils.sha1(message, timestamp.toString(), parent);
    }

//    public void trackStagingFiles(List<String> stagingFilesList) {
//
//    }

    public String getUID() {
        return UID;
    }

    public Map<String, String> getTrackingFile() {
        return trackingFile;
    }

//    public String toString() {
//        return this.message + this.timestamp + this.UID;
//    }
}

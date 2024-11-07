package gitlet;

// TODO: any imports you need here

import jdk.jshell.execution.Util;

import javax.xml.crypto.Data;
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
    private Commit parent;

    // file 存 Commit相关的Blob（uid)
    private Set<String> file;

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
        this.file = new HashSet<>();
//        System.out.println(timestamp.toString());
//        System.out.println(UID);
    }

    public String getUID() {
        return UID;
    }

    public Set<String> getFile() {
        return file;
    }

//    public String toString() {
//        return this.message + this.timestamp + this.UID;
//    }
}

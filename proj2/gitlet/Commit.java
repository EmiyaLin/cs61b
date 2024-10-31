package gitlet;

// TODO: any imports you need here

import javax.xml.crypto.Data;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
public class Commit {
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

    // The branch list
    private Map<String, Commit> branchs;

    // The current branch
    private String currentBranch;

    // The head pointer
    private Commit head;

    // TODO: UID to be finished, may be use
    private String UID;

    // TODO: FILE(blob)
    private List<Blob>

    public Commit() {
        message = "initial commit";
        parent = null;
        timestamp = new Date(0);
        currentBranch = "master";
        branchs = new TreeMap<>();
        branchs.put(currentBranch, this);
        head = this;
    }

    public Commit(String message, Commit parent, Date timestamp) {
        this.message = message;
        this.parent = parent;
        this.timestamp = timestamp;
    }

    private String getUID() {
        List<Object>
    }

}

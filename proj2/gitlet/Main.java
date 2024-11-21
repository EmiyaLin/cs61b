package gitlet;

import java.io.IOException;
import java.util.List;

import static gitlet.Utils.join;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length == 0) {
            System.out.println("Please enter a command");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if (Repository.GITLET_DIR.isDirectory()) {
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    System.exit(0);
                }
                try {
                    Repository.init();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                if (args[1] == null || !join(Repository.CWD, args[1]).isFile()) {
                    System.out.println("File does not exist.");
                    System.exit(0);
                }
                String filename = args[1];
                try {
                    Repository.add(filename);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            // TODO: FILL THE REST IN
            case "commit":
                if (args.length < 2) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                String message = args[1];
                try {
                    Repository.commit(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "rm":
                try {
                    Repository.rm(args[1]);
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globalLog();
                break;
            case "checkout":
                if (args.length == 2) {
                    String branchName = args[1];
                    Repository.checkoutBranch(branchName);
                } else if (args.length == 3 && args[1].equals("--")) {
                    filename = args[2];
                    Repository.checkout(filename);
                } else if (args.length == 4 && args[2].equals("--")) {
                    String commitUid = args[1];
                    filename = args[3];
                    Repository.checkout(commitUid, filename);
                }
                break;
            case "branch":
                if (args.length == 2) {
                    String branch = args[1];
                    Repository.createBranch(branch);
                }
                break;
            case "find":
                if (args.length == 2) {
                    String commitMessage = args[1];
                    Repository.find(commitMessage);
                }
        }
    }
}

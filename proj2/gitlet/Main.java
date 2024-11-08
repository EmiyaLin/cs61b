package gitlet;

import java.io.IOException;

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
                if (args[1] == null) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                String message = args[1];
                try {
                    Repository.commit(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
    }
}

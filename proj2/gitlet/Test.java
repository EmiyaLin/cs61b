package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static gitlet.Utils.join;

public class Test {
    @org.junit.Test
    public void test1() throws IOException {
        Commit head = Repository.getCurrentCommit();
        System.out.println(head.getTrackingFile().toString());
    }
}

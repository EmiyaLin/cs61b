package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static gitlet.Utils.join;

public class Test {
    @org.junit.Test
    public void test1() throws IOException {
        String uid = Utils.readContentsAsString(Repository.HEAD);
        System.out.println(uid);
        Commit content = Utils.readObject(join(Repository.GITLET_COMMIT, uid), Commit.class);
        System.out.println(content.toString());

    }
}

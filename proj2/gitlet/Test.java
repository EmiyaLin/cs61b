package gitlet;

import java.io.IOException;
import java.util.Date;

public class Test {
    @org.junit.Test
    public void test1() throws IOException {
        Repository.init();
        Repository.add("Hello.txt");
        Repository.commit("add Hello.txt");
    }
}

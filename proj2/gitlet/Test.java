package gitlet;

import java.io.IOException;

public class Test {
    @org.junit.Test
    public void test1() throws IOException {
        Repository.add("Hello.txt");
    }
}

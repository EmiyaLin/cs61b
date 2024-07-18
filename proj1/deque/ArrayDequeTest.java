package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {
    @Test
    public void ADequeTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>();
        for (int i = 0; i < 7; i ++) {
            arrayDeque.addFirst(i);
        }
        for (int i = 0; i < 7; i ++) {
            arrayDeque.removeFirst();
        }
        for (int i = 0; i < 7; i ++) {
            arrayDeque.addLast(i);
        }
        assertEquals(1, (int) arrayDeque.get(1));
    }
}

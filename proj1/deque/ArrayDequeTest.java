package deque;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {
    @Test
    public void ADequeTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>();
        arrayDeque.addLast(10);
        arrayDeque.addLast(8);
        arrayDeque.addFirst(1);
        arrayDeque.addFirst(2);
        assertEquals(1, (int) arrayDeque.get(1));
    }
}

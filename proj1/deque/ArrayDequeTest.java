package deque;

import jh61b.junit.In;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {
    @Test
    public void ADequeTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>();
        arrayDeque.addLast(1);
        arrayDeque.addLast(2);
        arrayDeque.addFirst(3);
        arrayDeque.addLast(4);
        arrayDeque.addLast(5);
        arrayDeque.addLast(6);
        arrayDeque.addLast(7);
        arrayDeque.addLast(8);
        assertEquals(2, (int) arrayDeque.get(2));
    }

    @Test
    public void ADQITest() {
        ArrayDeque<Integer> adq = new ArrayDeque<>();
        for (int i = 0; i < 5; i ++) {
            adq.addLast(i);
        }

        int checkValue = 0;
        for (int i : adq) {
            assertEquals("The value should be same", checkValue, i);
            checkValue += 1;
        }
    }

    @Test
    public void testResize() {
        ArrayDeque<Integer> adq = new ArrayDeque<>();
        for (int i = 0; i < 10; i ++) {
            adq.addFirst(i);
        }
        adq.addFirst(10);
    }
}

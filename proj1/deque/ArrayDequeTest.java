package deque;

import jh61b.junit.In;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

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
        ArrayDeque<String> adq = new ArrayDeque<>();
        for (int i = 0; i < 100; i++) {
            adq.addLast("String" + i);
        }

        int checkValue = 0;
        for (String i : adq) {
            assertEquals("The value should be same", "String" + checkValue, i);
            checkValue += 1;
        }
    }

    @Test
    public void testResize() {
        ArrayDeque<Integer> adq = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            adq.addFirst(i);
        }
        adq.addFirst(10);
        for (int i = 0; i < 11; i++) {
            assertEquals("should be same", 10 - i, (int) adq.removeFirst());
        }
    }

    @Test
    public void testAD_basic() {
        ArrayDeque<Integer> adq = new ArrayDeque<>();
        adq.isEmpty();
        adq.addFirst(1);
        adq.addFirst(2);
        adq.removeLast();
        adq.removeLast();
        adq.isEmpty();
        adq.isEmpty();
        adq.isEmpty();
        adq.isEmpty();
        adq.addFirst(9);
        assertEquals(9, (int) adq.removeLast());
    }

    @Test
    public void memTest() {
        ArrayDeque<Integer> adq = new ArrayDeque<>();
        for (int i = 0; i < 64; i++) {
            adq.addFirst(i);
        }
        for (int i = 0; i < 63; i++) {
            adq.removeFirst();
        }
        assertEquals(true, adq.size() <= 8);
    }

    @Test
    public void iteratorTest() {
        ArrayDeque<String> adq = new ArrayDeque<>();
        Iterator<String> adqIterator = adq.iterator();
        for (int i = 0; i < 10; i++) {
            adq.addLast("String" + i);
        }
        for (int i = 0; i < 10; i ++) {
            assertTrue("should be true", adqIterator.hasNext());
            assertEquals("String" + i, adqIterator.next());
        }
        assertFalse("should be false", adqIterator.hasNext());
    }
}

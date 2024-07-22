package deque;

import jh61b.junit.In;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {
    @Test
    public void testMax() {
        Comparator<Integer> cf = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        };
        Comparator<Integer> ct = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        MaxArrayDeque<Integer> maxArrayDeque = new MaxArrayDeque<Integer>(cf);
        maxArrayDeque.addFirst(10);
        maxArrayDeque.addFirst(8);
        maxArrayDeque.addLast(9);
        maxArrayDeque.addLast(20);
        assertEquals(20, (int) maxArrayDeque.max(ct));
    }
}

package deque;

import java.lang.reflect.Type;

public class LinkedListDeque {
    private class Deque<T> {
        private Deque<T> sentinel;
        private Deque prev;
        private Deque next;
        private T item;
        private int size;
        private boolean empty;

        public Deque() {
            
        }
        public Deque(T i, Deque p, Deque n) {
            item = i;
            prev = p;
            next = n;
            size = 1;
            empty = false;
        }

        public void addFirst(T item) {
            size += 1;
            empty = false;
        }
    }

    public LinkedListDeque() {
        sentinal = new Deque()
    }
}

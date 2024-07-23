package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;
    private int len = 10;

    public ArrayDeque() {
        items = (T[]) new Object[len];
        size = 0;
        first = 0;
        last = 0;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize((int) (size * 1.1));
        }
        if (items[first] != null) {
            if (first - 1 < 0) {
                first = len - 1;
            } else {
                first -= 1;
            }
        }
        items[first] = item;
        size += 1;
    }

    /*Todo: fix the bug when resizing the array deque*/
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        len = capacity;
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize((int) (size * 1.1));
        }
        if (items[last] != null) {
            if (last + 1 == len) {
                last = 0;
            } else {
                last += 1;
            }
        }
        items[last] = item;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (T item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T x = items[first];
        items[first] = null;
        first = (first + 1) % len;
        size -= 1;
        return x;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T x = items[last];
        items[last] = null;
        size -= 1;
        last = (last - 1 + len) % len;
        return x;
    }

    @Override
    public T get(int index) {
        if (index > size) {
            return null;
        }
        return items[(first + index) % len];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;
        public ArrayDequeIterator() {
            pos = first;
        }

        @Override
        public boolean hasNext() {
            return pos != last;
        }

        @Override
        public T next() {
            T returnItem = items[pos];
            pos = (pos + 1) % size;
            return returnItem;
        }
    }
}

package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int first;
    private int last;
    private int len = 8;

    public ArrayDeque() {
        items = (T[]) new Object[len];
        size = 0;
        first = 0;
        last = 0;
    }

    /*Todo: fix the bug when resizing the array deque*/
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        boolean direction = first <= last;
        if (direction) {
            System.arraycopy(items, first, a, 0, size);
        } else {
            int firstPartSize = len - first;
            System.arraycopy(items, first, a, 0, firstPartSize);
            System.arraycopy(items, 0, a, firstPartSize, last + 1);
        }
        first = 0;
        last = size - 1;
        len = capacity;
        items = a;
    }

    @Override
    public void addFirst(T item) {
        if (size == len) {
            resize((int) (len * 1.2));
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

    @Override
    public void addLast(T item) {
        if (size == len) {
            resize((int) (len * 1.2));
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
        if (len >= 16 && size * 1.0 / len <= 0.35) {
            resize(len / 2);
        }
        if (size == 0) {
            return null;
        }
        T x = items[first];
        items[first] = null;
        first = (first + 1) % len;
        size -= 1;
        if (size == 0) {
            first = 0;
            last = 0;
        }
        return x;
    }

    @Override
    public T removeLast() {
        if (len >= 16 && size * 1.0 / len <= 0.35) {
            resize(len / 2);
        }
        if (size == 0) {
            return null;
        }
        T x = items[last];
        items[last] = null;
        size -= 1;
        last = (last - 1 + len) % len;
        if (size == 0) {
            first = 0;
            last = 0;
        }
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
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Deque) {
            Deque deque = (Deque) o;
            if (deque.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (!this.get(i).equals(deque.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;
        private int check;

        ArrayDequeIterator() {
            pos = first;
            check = 0;
        }

        @Override
        public boolean hasNext() {
//            return (check != 0 && pos != first) && (pos != (last + 1) % size || pos == first);
            if (check != 0 && pos == first) {
                return false;
            }
            if (pos != (last + 1) % size || pos == first) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public T next() {
            T returnItem = items[pos];
            pos = (pos + 1) % size;
            check += 1;
            return returnItem;
        }
    }
}

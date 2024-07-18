package deque;

public class ArrayDeque<T> {
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

    public void addFirst(T item) {
        if (first - 1 < 0) {
            first = len - 1;
        } else {
            first -= 1;
        }
        items[first] = item;
        size += 1;
    }

    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, capacity);
        items = a;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize((int) (size * 1.1));
        }
        items[size] = item;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (T item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T x = items[0];
        T[] a = (T[]) new Object[size - 1];
        System.arraycopy(items, 1, a, 0, size - 1);
        size -= 1;
        items = a;
        return x;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T x = items[size - 1];
        items[size - 1] = null;
        size -= 1;
        return x;
    }

    public T get(int index) {
        if (index > size) {
            return null;
        }
        return items[(first + index) % len];
    }
}

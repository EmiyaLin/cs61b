package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    /**
     * sentinel.next指向循环队列的第一个节点、sentinel.prev指向循环队列的最后一个节点。
     */
    private Node sentinel;
    private int size;

    public class Node {
        private Node prev;
        private T item;
        private Node next;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;

        for (int i = 0; i < other.size(); i++) {
            addLast((T) other.get(i));
        }
    }

    /**
     * Adds an item of type T to the front of the deque.
     */
    @Override
    public void addFirst(T item) {
        size += 1;
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
    }

    /**
     * Adds an item of type T to the back of the deque.
     */
    @Override
    public void addLast(T item) {
        size += 1;
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
    }

    /**
     * Returns the number of items in the deque.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space. Once all the
     * items have been printed, print out a new line.
     */
    @Override
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(" " + p.item);
            p = p.next;
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            size--;
            T removedItem = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            return removedItem;
        }
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            size--;
            T removedItem = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            return removedItem;
        }
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else {
            Node p = sentinel.next;
            while (index != 0) {
                index--;
                p = p.next;
            }
            return p.item;
        }
    }

    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursiveHelper(index, sentinel.next);
    }

    private T getRecursiveHelper(int index, Node cur) {
        if (index == 0) {
            return cur.item;
        }
        return getRecursiveHelper(index - 1, cur.next);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Deque) {
            Deque deque = (Deque) o;
            if (deque.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < size; i ++) {
                if (this.get(i) != deque.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node h;

        public LinkedListDequeIterator() {
            h = sentinel.next;
        }
        @Override
        public boolean hasNext() {
            return h != null && h != sentinel;
        }

        @Override
        public T next() {
            T returnItem = h.item;
            h = h.next;
            return returnItem;
        }
    }
}

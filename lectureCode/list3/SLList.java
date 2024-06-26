public class SLList {
    public static class IntNode {
        public int item;
        public IntNode next;
        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }
    }

    private IntNode first;
    private int size;
    private IntNode sentinel;
    private IntNode last;

    public SLList() {
        first = null;
        size = 0;
        sentinel = new IntNode(-1, first);
    }

    public SLList(int x) {
        first = new IntNode(x, null);
        size = 1;
        sentinel = new IntNode(-1, first);
    }

    /** Adds an item to the front of the list. */
    public void addFirst(int x) {
        first = new IntNode(x, first);
        size += 1;
    }

    /** Retrieves the front item from the list. */
    public int getFirst() {
        return first.item;
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /** Adds an item to the end of the list. */
    public void addLast(int x) {
        size += 1;
        last.next = new IntNode(x, null);
        last = last.next;
    }

    /** Crashes when you call addLast on an empty SLList. Fix it. */
    public static void main(String[] args) {
        SLList x = new SLList();
        x.addLast(5);
    }
}
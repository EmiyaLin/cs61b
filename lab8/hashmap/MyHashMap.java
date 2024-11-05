package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int  initialSize = 16;
    private int bucketSize = initialSize;
    private double loadFactor = 0.75;
    private int size;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(initialSize);
        this.size = 0;
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        this.size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        this.loadFactor = maxLoad;
        this.size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] buckets = new Collection[tableSize];
        for (int i = 0; i < tableSize; i += 1) {
            buckets[i] = createBucket();
        }
        return buckets;
    }


    private void resize(int size) {
        bucketSize *= 2;
        Collection<Node>[] temp = createTable(size * 2);
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                int i = hash(node.key);
                temp[i].add(createNode(node.key, node.value));
            }
        }
        buckets = temp;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    public void clear() {
        for (Collection<Node> bucket : buckets) {
            bucket.clear();
        }
        this.size = 0;
    }

    private int hash(K key) {
        return Math.floorMod(key.hashCode(), bucketSize);
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V get(K key) {
        if (key == null)    return null;
        for (Node node : buckets[hash(key)]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    public int size() {
        return this.size;
    }

    public void put(K key, V value) {
        if (!containsKey(key)) {
            int i = hash(key);
            buckets[i].add(createNode(key, value));
            size += 1;
            if ((double) size / bucketSize > loadFactor) {
                resize(bucketSize);
            }
        } else {
            int i = hash(key);
            for (Node node : buckets[i]) {
                if (node.key.equals(key)) {
                    node.value = value;
                }
            }
        }
    }

    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for(int i = 0; i < buckets.length; i += 1) {
            Iterator<Node> iterator = buckets[i].iterator();
            while (iterator.hasNext()) {
                set.add(iterator.next().key);
            }
        }
        return set;
    }

    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}

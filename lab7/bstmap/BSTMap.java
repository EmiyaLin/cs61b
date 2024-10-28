package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BST bst;

    public BSTMap() {
        bst = new BST();
    }
    @Override
    public void clear() {
        bst.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return bst.contains(key);
    }

    @Override
    public V get(K key) {
        return bst.get(key);
    }

    @Override
    public int size() {
        return bst.size();
    }

    @Override
    public void put(K key, V value) {
        bst.put(key, value);
    }

    public void printInOrder() {
        bst.printInOrder();
    }

    // ---------------------------------throw Exception in under function---------------------------------------//
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private class BST {
        private Node root;
        private class Node {
            private K key;
            private V val;
            private Node left, right;
            private int size;
            public Node(K key, V val, int size) {
                this.key = key;
                this.val = val;
                this.size = size;
            }
        }

        public BST() {

        }

        public int size() {
            return size(root);
        }

        private int size(Node x) {
            if (x == null)  return 0;
            return x.size;
        }

        public V get(K key) {
            return get(root, key);
        }

        private V get(Node x, K key) {
            if (x == null)      return null;
            int cmp = key.compareTo(x.key);
            if (cmp < 0)        return get(x.left, key);
            else if (cmp > 0)   return get(x.right, key);
            else                return x.val;
        }

        public boolean contains(K key) {
            return contains(root, key);
        }

        private boolean contains(Node x, K key) {
            if (x == null)  return false;
            int cmp = key.compareTo(x.key);
            if (cmp < 0)            return contains(x.left, key);
            else if (cmp > 0)       return contains(x.right, key);
            else                    return true;
        }

        public void put(K key, V val) {
            if (contains(key)) return;
            root = put(root, key, val);
        }

        private Node put(Node x, K key, V val) {
            if (x == null)  return new Node(key, val, 1);
            int cmp = key.compareTo(x.key);
            if (cmp < 0)        x.left = put(x.left, key, val);
            else if (cmp > 0)   x.right = put(x.right, key, val);
            x.size = size(x.left) + size(x.right) + 1;
            return x;
        }

        public void clear() {
            root = null;
        }

        public void printInOrder() {
            printInOrder(root);
        }

        private void printInOrder(Node x) {
            if (x == null)  return;
            printInOrder(x.left);
            System.out.println(x.key);
            printInOrder(x.right);
        }
    }
}

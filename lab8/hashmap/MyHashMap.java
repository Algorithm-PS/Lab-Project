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
    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int elementCount;
    private int hashTableSize;
    private double maxLoad;


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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(key, node.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

    /** Constructors */
    public MyHashMap() {
        this.elementCount = 0;
        this.maxLoad = 0.75;
        this.hashTableSize = 16;
        this.buckets = (Collection<Node>[]) new Collection[16];
        for (int i = 0; i < 16; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        this.elementCount = 0;
        this.maxLoad = 0.75;
        this.hashTableSize = initialSize;
        this.buckets = (Collection<Node>[]) new Collection[initialSize];
        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.elementCount = 0;
        this.maxLoad = maxLoad;
        this.hashTableSize = initialSize;
        this.buckets = (Collection<Node>[]) new Collection[initialSize];
        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }



    @Override
    public void clear() {
        this.elementCount = 0;
        this.maxLoad = 0.75;
        this.hashTableSize = 16;
        this.buckets = (Collection<Node>[]) new Collection[16];
        for (int i = 0; i < 16; i++) {
            buckets[i] = createBucket();
        }
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        int i = hash(key);
        Collection<Node> currentBucket = buckets[i];
        Iterator<Node> iterator = currentBucket.iterator();
        while(iterator.hasNext()) {
            Node next = iterator.next();
            if (key.equals(next.key)) {
                return next.value;
            }
        }
        return null;
    }

    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12) ^ (h >>> 7) ^ (h >>> 4);
        return h & (hashTableSize - 1);
    }

    @Override
    public int size() {
        return elementCount;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");

        int currentLoad = elementCount / buckets.length;
        if (currentLoad >= maxLoad) {
            resize();
        }

        int i = hash(key);
        Node node = createNode(key, value);
        if (!buckets[i].contains(node)) {
            elementCount++;
            buckets[i].add(createNode(key, value));
        }
        else {
            buckets[i].remove(createNode(key, value));
            buckets[i].add(createNode(key, value));
        }
    }

    private void resize() {
        MyHashMap<K, V> temp = new MyHashMap<>(hashTableSize * 2, maxLoad);
        for (int i = 0; i < hashTableSize; i++) {
            Iterator<Node> currentIterator = buckets[i].iterator();
            while (currentIterator.hasNext()) {
                Node currentNode = currentIterator.next();
                temp.put(currentNode.key, currentNode.value);
            }
        }
        this.hashTableSize  = temp.hashTableSize;
        this.elementCount  = temp.elementCount;
        this.buckets = temp.buckets;
    }

    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();
        for (int i = 0; i < hashTableSize; i ++) {
            Iterator<Node> currentIterator = buckets[i].iterator();
            while (currentIterator.hasNext()) {
                Node currentNode = currentIterator.next();
                result.add(currentNode.key);
            }
        }
        return result;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return null;
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
        return new LinkedList<>();
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
        return null;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}

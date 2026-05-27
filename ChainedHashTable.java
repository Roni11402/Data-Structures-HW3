import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChainedHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 2;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private List<Element<K, V>>[] table;
    private int size;
    private int k;

    public ChainedHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ChainedHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new List[this.capacity];
        this.size = 0;
        this.k = k;
    }

    public V search(K key) {
        int index = hashFunc.hash(key);
        if (table[index] != null) {
            Iterator<Element<K, V>> iter = table[index].iterator();
            while (iter.hasNext()) {
                Element<K, V> curr = iter.next();
                if (curr.key().equals(key)) {
                    return curr.satelliteData();
                }
            }
        }
        return null;
    }

    public void insert(K key, V value) {
        Element<K, V> toAdd = new Element<K, V>(key, value);
        int index = hashFunc.hash(key);
        boolean found = false;
        if (table[index] != null) {
            Iterator<Element<K, V>> iter = table[index].iterator();
            while (iter.hasNext() && !found) {
                Element<K, V> curr = iter.next();
                if (curr.key().equals(key)) {
                    curr.setSatData(value);
                    found = true;
                    return;
                }
            }
        }
        size = size + 1;
        if (((double) size / capacity) >= this.maxLoadFactor) {
            rehashing();
            index = hashFunc.hash(key);
        }
        if (table[index] != null) {
            table[index].add(toAdd);
        } else {
            table[index] = new LinkedList<>();
            table[index].add(toAdd);
        }
    }


    public boolean delete(K key) {
        int index = hashFunc.hash(key);
        if (table[index] == null) {
            return false;
        } else {
            Iterator<Element<K, V>> iter = table[index].iterator();
            while (iter.hasNext()) {
                Element<K, V> curr = iter.next();
                if (curr.key().equals(key)) {
                    iter.remove();
                    size = size - 1;
                    return true;
                }
            }
        }
        return false;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() {
        return capacity;
    }

    public void rehashing() {
        this.capacity = 2 * this.capacity;
        List<Element<K, V>>[] oldTable = table;
        this.table = new List[this.capacity];
        this.k = k + 1;
        this.hashFunc = hashFactory.pickHash(k);
        this.size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Iterator<Element<K, V>> iter = oldTable[i].iterator();
                while (iter.hasNext()) {
                    Element<K, V> curr = iter.next();
                    insert(curr.key(), curr.satelliteData());
                }
            }
        }
    }
}



import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class ProbingHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private Element<K, V>[] table;
    private Element<K, V> deleted;
    private int size;
    private int k;

    /*
     * You should add additional private fields as needed.
     */

    public ProbingHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new Element[capacity];
        this.deleted = new Element<>(null, null);
        this.size = 0;
        this.k = k;

    }

    public ProbingHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public V search(K key) {
        int index = hashFunc.hash(key);
        if (table[index] == null) {
            return null;
        }
        if (table[index].key() != null && table[index].key().equals(key)) {
            return table[index].satelliteData();
        }
        for (int i = index + 1; i != index; i = (i + 1) % capacity) {
            if (table[i] == null) {
                return null;
            }
            if (table[i].key() != null && table[i].key().equals(key)) {
                return table[i].satelliteData();
            }
        }
        return null;
    }

    public void insert(K key, V value) {
        if (size == capacity) {
            rehashing();
        }
        int index = hashFunc.hash(key);
        int firstDelete = -1;
        while (table[index] != null) {
            if (table[index] == deleted && firstDelete == -1) {
                firstDelete = index;
            } else {
                if (table[index].key().equals(key)) {
                    table[index].setSatData(value);
                    return;
                }
            }
            index = (index + 1) % capacity;
        }
        Element<K, V> toInsert = new Element<>(key, value);
        if (firstDelete != -1) {
            table[firstDelete] = toInsert;
            size++;
        } else {
            table[index] = toInsert;
            size++;
        }
    }

    public boolean delete(K key) {
        int index = hashFunc.hash(key);
        while (table[index] != null) {
            if (table[index] != deleted && table[index].key().equals(key)) {
                table[index] = deleted;
                size--;
                return true;
            }
            index = (index + 1) % capacity;
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
        Element<K, V>[] oldTable = table;
        this.table = new Element[capacity];
        this.k = k + 1;
        this.hashFunc = hashFactory.pickHash(k);
        this.size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null && oldTable[i] != deleted) {
                Element<K, V> curr = oldTable[i];
                insert(curr.key(), curr.satelliteData());
            }
        }
    }
}


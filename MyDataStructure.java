import com.sun.net.httpserver.Filter;

import java.util.List;
import java.util.ArrayList;

public class MyDataStructure {
    private ModularHash hashFactory;
    private ChainedHashTable hashTable;
    private IndexableSkipList skipList;

    /***
     * This function is the Init function described in Part 4.
     *
     * @param N The maximal number of items that may reside in the DS.
     */

    //Initiating a new Hash Table for Θ(N) and a skipList for Θ(1)
    //The "while" loop runs for Θlogn, and creating an array that is in size k*k is Θ(N) as required
    public MyDataStructure(int N) {
        this.hashFactory = new ModularHash();
        int k = 0;
        while ((1 << k) < N) {
            k++;
        }
        this.hashTable = new ChainedHashTable(hashFactory, k, 2.0);
        this.skipList = new IndexableSkipList(0.5);
    }


    //in this function, we will insert the values in to an Indexable skip list for an expected runtime
    //of Θ(logn) expected, as learned in class
    // We will also insert it into the Hash table for an expected runtime of Θ(1) expected as learned in
    //class.
    // for a quick search and keep pointers in the hash table to shorten access times.
    public boolean insert(int value) {
        AbstractSkipList.SkipListNode inserted = this.skipList.insert(value);
        if (inserted != null) {
            this.hashTable.insert(value, inserted);
            return true;
        } else {
            return false;
        }

    }

    //in this function, we will first search the key in the hash table, which allows us to keep a runtime
    //of Θ(1) expected in case val isn't in the DS, since search time in a hash table is Θ(1) expected.
    // if it is, we will remove it from both DS for Θ(logn) expected, as learned in class.
    public boolean delete(int value) {
        AbstractSkipList.SkipListNode toDelete = (AbstractSkipList.SkipListNode) hashTable.search(value);
        if (toDelete == null) {
            return false;
        } else {
            skipList.delete(toDelete);
            hashTable.delete(value);
            return true;
        }
    }

    //In this function we will perform a search in the hash table for Θ(1) expected, as learned in class
    public boolean contains(int value) {
        if (hashTable.search(value) == null) {
            return false;
        } else {
            return true;
        }
    }

    //In this function we will use the rank function of the Indexable skip list to return the rank for
    //Θ(logn) expected, as we learned in class
    public int rank(int value) {
        return skipList.rank(value);
    }

    //In this function we will use the select function of the Indexable skip list to return the index for
    //Θ(logn) expected, as we learned in class
    public int select(int index) {
        return skipList.select(index);
    }

    //In this function we are checking to see if the low key is in the DS - if not, we will return null,
    //for an expected runtime of Θ(1), searching in a hash table.
    //If it is in the DS, We will go over all keys in ascending order until reaching the "high" key.
    //Since the loop runs on all keys in "L", runtime will be Θ(|L|) expected.
    public List<Integer> range(int low, int high) {
        AbstractSkipList.SkipListNode curr = (AbstractSkipList.SkipListNode) hashTable.search(low);
        if (curr == null) {
            return null;
        }
        List<Integer> L = new ArrayList<>();
        while (curr.key() <= high) {
            L.add(curr.key());
            curr = skipList.successor(curr);
        }
        return L;
    }
}

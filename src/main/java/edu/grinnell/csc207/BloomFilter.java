package edu.grinnell.csc207;

import java.util.List;
import java.util.function.Function;

/**
 * A Bloom Filter is a probabilistic data structure that efficiently tests
 * set membership with the possibility of false positives.
 */
public class BloomFilter<T> {
    private int numBits;
    private List<Function<T, Integer>> hashes;

    /**
     * Constructs a new Bloom Filter with the given number of bits and hash functions.
     * @param numBits the number of bits utilized in the filter
     * @param hashes the list of hash functions utilized by the filter
     */
    public BloomFilter(int numBits, List<Function<T, Integer>> hashes) {
        this.numBits = numBits;
        this.hashes = hashes;
    }

    /** @param item the item to add to the Bloom Filter */
    public void add(T item) {
        for (int i = 0; i < hashes.size(); i++){
            hashes.get(i).apply(item);
        }
    }

    /**
     * @param item the item to check for membership in the Bloom filter
     * @return true if the item is (possibly) in the Bloom filter and false if
     * it is definitely not in the filter.
     */
    public boolean contains(T item) {
        for (int i = 0; i < hashes.size(); i++){
            if(hashes.get(i).equals(item)){
                return true;
            }
        } return false;
    }
}

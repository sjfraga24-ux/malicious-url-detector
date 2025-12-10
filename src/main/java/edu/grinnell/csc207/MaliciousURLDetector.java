package edu.grinnell.csc207;
import java.util.concurrent.ThreadLocalRandom;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.hash.*;

/**
 * A simple malicious URL detector program that utilizes a Bloom Filter and a
 * dataset of known malicious URLs to efficiently check whether a URL is
 * potentially malicious.
 */
public class MaliciousURLDetector {
    // From: https://www.kaggle.com/datasets/sid321axn/malicious-urls-dataset
    public static final String DATA_PATH = "data/malicious_phish.csv";

    /**
     * Creates a list of <code>num</code> string hash functions utilizing the
     * Murmur3 hashing algorithm.
     * @param num the number of hash functions
     * @return a list of <code>num</code> string hash functions
     */
    public static List<Function<String, Integer>> makeStringHashFunctions(int num) {
        List<Function<String, Integer>> lst = new ArrayList<Function<String, Integer>>();
        while (num > 0){
            int x = ThreadLocalRandom.current().nextInt();
            HashFunction hf = Hashing.murmur3_128(x);
            num--;
            Function<String, Integer> n = (String input)-> {
                
                return hashString(input,Charset.defaultCharset());
            };
            // HashCode code = Hashing.hashString(input,Charset.defaultCharset());
            lst.add(n);
            return lst;
        }
    }

    /**
     * @param numBits the number of bits dedicated to the filter
     * @param numHashFunctions the number of hash functions to use
     * @return a Bloom filter for detecting malicious URLs.
     */
    public static BloomFilter<String> makeURLFilter(int numBits, int numHashFunctions) throws FileNotFoundException {
        Scanner scan = new Scanner (DATA_PATH);
        scan.useDelimiter(",");
        List<Function<String, Integer>> lst = makeStringHashFunctions(numHashFunctions);
        while (scan.hasNext ()){
            String str = scan.next();
            String type = scan.next();
            if(!type.equals("benign")){
                for (int i = 0; i < lst.size (); i++){
                    lst.get(i).apply(str);
                }
            }
            
        }

        BloomFilter<String> bloom = new BloomFilter<String>(numBits, lst);
        return bloom;
    }

    /**
     * The main method for the program.
     * @param args the arguments to the program
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.err.println("Usage: java MaliciousURLDetector <numBits> <numHashFunctions>");
            return;
        }
        BloomFilter<String> data = makeURLFilter(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        Scanner scan = new Scanner(System.in);
        String str = "";
        boolean running = true;
        while(running){
            str = scan.next ();
            if (str.equals ("exit")){
                running = false;
            } else {
                System.out.println (data.contains(str));
            }
        }

    }
}

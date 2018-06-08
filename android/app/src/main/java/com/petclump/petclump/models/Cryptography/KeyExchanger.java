package com.petclump.petclump.models.Cryptography;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.petclump.petclump.controller.ContextProvider;
import com.petclump.petclump.controller.MainActivity;

import java.math.*;
import java.util.*;
import java.security.*;

import java.io.*;


public class KeyExchanger {

    BigInteger primitiveRoot, bigPrime;
    private BigInteger mySecret;
    String friendId;
    byte[] sharedKey = null;

    /**
     * This constructor should generate the values for you and your friends
     * @param friendId generate a secret number for this id
     */
    public KeyExchanger(String friendId){
        this.friendId = friendId;
        generatePublicPrimes();
        loadOrGenerateSecret(friendId);
    }

    /**
     * This constructor should take the public variables
     * @param bigPrime the agreed public big prime between you and friend
     * @param primitiveRoot the agreed public primitive root between you and friend
     * @throws Exception Not going to do anything as of now
     */
	public KeyExchanger(String friendId, BigInteger friendPublic, BigInteger bigPrime, BigInteger primitiveRoot) throws Exception {
        this.friendId = friendId;
		this.bigPrime = bigPrime;
		this.primitiveRoot = primitiveRoot;
        loadOrGenerateSecret(friendId);
	}


	public KeyExchanger(String acceptFriendId, Map<String, Object> data){
        this.friendId = acceptFriendId;
        this.bigPrime = new BigInteger(data.get("bigPrime").toString());
        this.primitiveRoot = new BigInteger(data.get("priPrime").toString());
        loadOrGenerateSecret(friendId);
    }

    /**
     * This method computes the shared key with a given friend public number
     * @param fdPublic the public number from a friend, should match the id of the constructor
     * @return a byte array of the shared key
     * @throws Exception Not going to do anything as of now
     */
	public byte[] getSharedKey(BigInteger fdPublic) throws Exception{
        if (sharedKey == null) {
            byte[] key = fdPublic.modPow(mySecret, bigPrime).toString().getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            sharedKey = Arrays.copyOf(key, 16);
        }
        return sharedKey;
    }

    private void loadOrGenerateSecret(String fdId){

        if (mySecret == null){
            Context c = ContextProvider.getContext();
            File file = new File(c.getFilesDir(),fdId+".txt");

            if(file.exists()){

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    mySecret = new BigInteger(br.readLine());
                    Log.d("loadOrGenerateSecret","hit:"+fdId+"\n"+mySecret.toString());
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2){
                    e2.printStackTrace();
                }
            }else{
                mySecret = new BigInteger(500, new Random());
                try {
                    Log.d("loadOrGenerateSecret", "create:"+file.getAbsolutePath());
                    file.createNewFile();

                    BufferedWriter wr = new BufferedWriter(new FileWriter(file));
                    wr.write(mySecret.toString());
                    Log.d("loadOrGenerateSecret:","store:"+fdId+"\n"+mySecret.toString());
                    wr.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public BigInteger getPrimitiveRoot() {
        return primitiveRoot;
    }

    public BigInteger getBigPrime() {
        return bigPrime;
    }

    public BigInteger getMyPublic() {
        return primitiveRoot.modPow(mySecret, bigPrime);
    }

    public void generatePublicPrimes(){
	    this.bigPrime = PrimeUtil.generate512bitProbabilisticPrime();
	    this.primitiveRoot = PrimeUtil.findPrimitiveRoot(this.bigPrime);
    }
}

class PrimeUtil{

    private static final int bitLength = 512;
    private static final int certainty = 20;
    private static final SecureRandom rnd = new SecureRandom();

    /**
     * This method generates a big inter of 512 bits using java BigInteger
     * @return a big probabilistic prime
     */
    public static BigInteger generate512bitProbabilisticPrime(){
        return new BigInteger(bitLength, certainty, new SecureRandom());
    }

    /**
     * This method uses miller rabin algorithm to check if a number is prime
     * @param r a big integer number
     * @return if it's a prime number
     */
    public static boolean isPrime(BigInteger r) {
        return PrimeUtil.miller_rabin(r);
    }

    /**
     * This method finds a primitive root of a number (Relatively prime)
     * @param p number to find primitive root
     * @return a big integer of a primitive root
     */
    public static BigInteger findPrimitiveRoot(BigInteger p) {
        Random rn = new Random();
        int start = rn.nextInt(50000);
        for (int i = start; i < 100000000; i++)
            if (isPrimeRoot(BigInteger.valueOf(i), p)) {
                return BigInteger.valueOf(i);
            }
        return BigInteger.valueOf(0);
    }

    private static boolean miller_rabin_pass(BigInteger a, BigInteger n) {
        BigInteger n_minus_one = n.subtract(BigInteger.ONE);
        BigInteger d = n_minus_one;
        int s = d.getLowestSetBit();
        d = d.shiftRight(s);
        BigInteger a_to_power = a.modPow(d, n);
        if (a_to_power.equals(BigInteger.ONE)) return true;
        for (int i = 0; i < s - 1; i++) {
            if (a_to_power.equals(n_minus_one)) return true;
            a_to_power = a_to_power.multiply(a_to_power).mod(n);
        }
        if (a_to_power.equals(n_minus_one)) return true;
        return false;
    }

    private static boolean miller_rabin(BigInteger n) {
        for (int repeat = 0; repeat < 20; repeat++) {
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), rnd);
            } while (a.equals(BigInteger.ZERO));
            if (!miller_rabin_pass(a, n)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPrimeRoot(BigInteger g, BigInteger p) {
        BigInteger totient = p.subtract(BigInteger.ONE);
        List < BigInteger > factors = primeFactors(totient);
        int i = 0;
        int j = factors.size();
        for (; i < j; i++) {
            BigInteger factor = factors.get(i);
            BigInteger t = totient.divide(factor);
            if (g.modPow(t, p).equals(BigInteger.ONE)) return false;
        }
        return true;
    }

    private static List<BigInteger> primeFactors(BigInteger number) {
        BigInteger n = number;
        BigInteger i = BigInteger.valueOf(2);
        BigInteger limit = BigInteger.valueOf(10000);
        List<BigInteger> factors = new ArrayList < BigInteger > ();
        while (!n.equals(BigInteger.ONE)) {
            while (n.mod(i).equals(BigInteger.ZERO)) {
                factors.add(i);
                n = n.divide(i);
                if (PrimeUtil.isPrime(n)) {
                    factors.add(n);
                    return factors;
                }
            }
            i = i.add(BigInteger.ONE);
            if (i.equals(limit)) {
                return factors;
            }
        }
        return factors;
    }
}
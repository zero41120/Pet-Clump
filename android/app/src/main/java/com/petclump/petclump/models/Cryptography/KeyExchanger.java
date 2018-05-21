package com.petclump.petclump.models.Cryptography;

import java.math.*;
import java.util.*;
import java.security.*;

import java.io.*;


public class KeyExchanger {

    BigInteger primitiveRoot, bigPrime;
    private BigInteger mySecret;
    byte[] sharedKey = null;
    /**
     * This constructor should generate the values for you and your friends
     * @param friendId generate a secret number for this id
     */
    public KeyExchanger(String friendId){
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

		this.bigPrime = bigPrime;
		this.primitiveRoot = primitiveRoot;
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
            // TODO: Load from file for fdPublic, use a fix seed of fdId for now
            long seed = 0;
            for (char c : fdId.toCharArray()) {
                seed = 31L*seed + c;
            }
            mySecret = new BigInteger(500, new Random(seed));
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

    private static final int bitLength = 20;
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
        int start = 2001;
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
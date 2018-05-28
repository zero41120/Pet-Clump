package com.petclump.petclump;


import com.petclump.petclump.models.Cryptography.Cryptographer;
import com.petclump.petclump.models.Cryptography.KeyExchanger;

import org.junit.Test;


import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MessageEncryptUnitTest {

    @Test
    public void encryptionAES() throws Exception {

        Cryptographer cG = Cryptographer.getInstance();

        byte[] key = cG.generateSecretKey();
        byte[] iv  = cG.generateInitializationVector();

        String myMessage = "你好 \uD83C\uDF5A  الكلب UTF-8";
        String cipherText = cG.encrypt(key, iv, myMessage);
        String plainText  = cG.decrypt(key, iv, cipherText);

        assertEquals(myMessage, plainText);
    }

    @Test
    public void keyExchangeDH() throws Exception{
        // Alice generates public primes and her public number
        KeyExchanger alice = new KeyExchanger("bobid");
        BigInteger bigPrime = alice.getBigPrime();
        BigInteger priPrime = alice.getPrimitiveRoot();
        BigInteger alicePublic = alice.getMyPublic();

        // Bob uses the public number and primes to generate a public number
        KeyExchanger bob = new KeyExchanger("aliceid", alicePublic, bigPrime, priPrime);
        BigInteger bobPublic = bob.getMyPublic();

        // Both user generate a shared key with respect to public numbers
        byte[] bobShared = bob.getSharedKey(alicePublic);
        byte[] aliceShared = alice.getSharedKey(bobPublic);

        assertArrayEquals(bobShared, aliceShared);
    }

    @Test
    public void encryptWithDHKey() throws Exception{

        // Alice generates public primes and her public number
        KeyExchanger alice = new KeyExchanger("bobid");
        BigInteger bigPrime = alice.getBigPrime();
        BigInteger priPrime = alice.getPrimitiveRoot();
        BigInteger alicePublic = alice.getMyPublic();

        // Bob and Eve uses the public number and primes to generate a public number
        KeyExchanger bob = new KeyExchanger("aliceid", alicePublic, bigPrime, priPrime);
        BigInteger bobPublic = bob.getMyPublic();
        KeyExchanger eve = new KeyExchanger("aliceid", alicePublic, bigPrime, priPrime);

        // Generates shared keys
        byte[] aliceShared = alice.getSharedKey(bobPublic);
        byte[] bobShared = bob.getSharedKey(alicePublic);
        byte[] eveShared = eve.getSharedKey(alicePublic);

        assertArrayEquals(bobShared, aliceShared);
        assertFalse(Arrays.equals(aliceShared, eveShared));

        Cryptographer cG = Cryptographer.getInstance();
        byte[] iv  = cG.generateInitializationVector();

        // Encrypt and decrypt message
        String myMessage = "你好 \uD83C\uDF5A  الكلب UTF-8";
        String cipherText = cG.encrypt(bobShared, iv, myMessage);
        String plainText  = cG.decrypt(aliceShared, iv, cipherText);
        String brokenText = cG.decrypt(eveShared, iv, cipherText);

        assertEquals(myMessage, plainText);
        assertNotEquals(brokenText, myMessage);

    }

}
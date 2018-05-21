package com.petclump.petclump;


import com.petclump.petclump.models.Cryptography.Cryptographer;
import com.petclump.petclump.models.Cryptography.KeyExchanger;

import org.junit.Test;


import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class MessageEncryptUnitTest {

    @Test
    public void encryptionTestAES() throws Exception {

        Cryptographer cG = Cryptographer.getInstance();

        byte[] key = cG.generateSecretKey();
        byte[] iv  = cG.generateInitializationVector();

        String myMessage = "你好 \uD83C\uDF5A  الكلب UTF-8";
        String cipherText = cG.encrypt(key, iv, myMessage);
        String plainText  = cG.decrypt(key, iv, cipherText);

        assertEquals(myMessage, plainText);
    }

    @Test
    public void keyExchangeTextDH() throws Exception{
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

}
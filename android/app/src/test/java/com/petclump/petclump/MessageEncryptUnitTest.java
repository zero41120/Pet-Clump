package com.petclump.petclump;


import com.petclump.petclump.models.Cryptography.Cryptographer;

import org.junit.Test;


import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class MessageEncryptUnitTest {

    @Test
    public void encryptionTest() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        Cryptographer cG = Cryptographer.getInstance();

        byte[] key = cG.generateSecretKey();
        byte[] iv  = cG.generateInitializationVector();

        String myMessage = "你好 \uD83C\uDF5A  الكلب UTF-8";
        String cipherText = cG.encrypt(key, iv, myMessage);
        String plainText  = cG.decrypt(key, iv, cipherText);

        assertEquals(myMessage, plainText);
    }


}
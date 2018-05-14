package com.petclump.petclump.models.Cryptography;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Cryptographer {

    private static final String  CIPHER_ALGRO = "AES/CBC/PKCS5PADDING";
    private static final Cryptographer instance = new Cryptographer();

    private Cryptographer(){ }

    public static Cryptographer getInstance() {
        return instance;
    }

    public byte[] generateSecretKey(){
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128) ;
            byte[] key = keygen.generateKey().getEncoded();
            System.out.println("Generated a Secret Key: " + new String(key, "UTF-8"));
            return key;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public byte[] generateInitializationVector(){
        try {
            byte initVector[] = new byte[16];
            SecureRandom secRandom = new SecureRandom() ;
            secRandom.nextBytes(initVector);
            System.out.println("Init Vector: " +  new String(initVector, "UTF-8"));
            return initVector;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String encrypt(byte[] key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv     = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_ALGRO);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decrypt(byte[] key, byte[] initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_ALGRO);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}

package com.petclump.petclump.models.Cryptography;

import android.util.Base64;

import com.google.common.primitives.UnsignedBytes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * https://www.veracode.com/blog/research/encryption-and-decryption-java-cryptography
 */
public class Cryptographer {

    private static final String  CIPHER_ALGRO   = "AES/CBC/PKCS5PADDING";
    private static final Integer KEY_BITS       = 128;
    private static final Integer IV_BYTES       = 16;

    /**
     * This is a singleton class.
     */
    private static final Cryptographer instance = new Cryptographer();
    private Cryptographer(){ }
    public static final Cryptographer getInstance() {
        return instance;
    }

    /**
     * This method generates a secret key.
     * We have to make Diffie-Hellman exchange for the shared key.
     * @return a byte array of the secret key
     */
    public final byte[] generateSecretKey(){
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(KEY_BITS);
            byte[] key = keygen.generateKey().getEncoded();
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method generates a initialization vector.
     * We will store this value for each message we send.
     * @return a byte array of the IV
     */
    public final byte[] generateInitializationVector(){
        byte initVector[]      = new byte[IV_BYTES];
        SecureRandom secRandom = new SecureRandom();
        secRandom.nextBytes(initVector);
        return initVector;
    }

    /**
     * This method encrypt a UTF-8 message.
     * @param key - a shared secret key for encrypt
     * @param initVector - the IV for encrypt
     * @param plainText - the message to encrypt
     * @return a Base64 cipher text to store in the cloud
     */
    public final String encrypt(byte[] key, byte[] initVector, String plainText) {
        try {
            IvParameterSpec iv     = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_ALGRO);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(plainText.getBytes());

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * This method decrypts a cipher text.
     * @param key - a shared secret key for encrypt
     * @param initVector - the IV for encrypt
     * @param cipherText - a encrypted message
     * @return a String of plain text
     */
    public final String decrypt(byte[] key, byte[] initVector, String cipherText) {
        try {
            IvParameterSpec iv     = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_ALGRO);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT));

            return new String(original);
        } catch (BadPaddingException ex) {
            return "Error Padding";
        } catch (Exception e) {
            return "Error";
        }
    }

    public static final String convertIV(byte[]iv) {
        // Conform ios style
        String temp = "";
        for (byte i: iv) {
            int unsignedByte = i & 0xFF;
            temp += " " + unsignedByte + ",";
        }
        // Removes leading ' ' and tailing ','
        String out = "[" + temp.substring(1, temp.length() -1) + "]";
        return out;
    }

    public static final byte[] convertIV(String iv){
        String parse = iv.substring(1,iv.length()-1);
        parse = parse.replace(" ", "");
        String[] segments = parse.split(",");
        byte[] bytes = new byte[segments.length];
        for (int i = 0; i < segments.length; i++) {
            bytes[i] = (byte) Integer.parseInt(segments[i]);
        }
        return bytes;
    }
}

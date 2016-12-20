package com.auction.utils;


import org.apache.commons.io.IOUtils;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Utils {

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int PBKDF2_ITERATIONS = 1000;

    public static String getPwdHash(String pwd, String salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(pwd.toCharArray(), salt.getBytes(), PBKDF2_ITERATIONS, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return toHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean validatePassword(String pwd, String salt, String storedPassword) {
        try {
            byte[] hash = fromHex(storedPassword);
            PBEKeySpec spec = new PBEKeySpec(pwd.toCharArray(), salt.getBytes(), PBKDF2_ITERATIONS, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for (int i = 0; i < hash.length && i < testHash.length; i++) {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static String toString(InputStream is) {
        try {
            return IOUtils.toString(is, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

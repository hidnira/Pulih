/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A utility class that provides a static method for hashing passwords.
 * This class uses the SHA-1 hashing algorithm to convert plain-text passwords
 * into a secure hash for storage.
 */

public class PasswordHasher {

    /**
     * Converts a plain-text password into its SHA-1 hash representation.
     * This is a one-way process; the original password cannot be recovered from the hash.
     * @param passwordToHash The plain-text password to be hashed.
     * @return A string containing the hexadecimal representation of the password hash,
     * or null if the hashing algorithm is not found.
     */

    public static String hashPassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
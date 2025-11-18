// src/main/java/org/example/util/UserServiceUtil.java
package org.example.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class UserServiceUtil {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Hash a password with salt for secure storage
     */
    public static String hashPassword(String password) {
        try {
            // Generate salt
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Combine salt and hash
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Check if a password matches the stored hash
     */
    public static boolean checkPassword(String password, String storedHash) {
        try {
            byte[] combined = Base64.getDecoder().decode(storedHash);

            // Extract salt (first 16 bytes)
            byte[] salt = new byte[16];
            System.arraycopy(combined, 0, salt, 0, 16);

            // Hash the provided password with the same salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Extract stored hash (remaining bytes)
            byte[] storedPasswordHash = new byte[hashedPassword.length];
            System.arraycopy(combined, 16, storedPasswordHash, 0, hashedPassword.length);

            // Compare hashes
            return MessageDigest.isEqual(hashedPassword, storedPasswordHash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generate a unique user ID
     */
    public static String generateUserId() {
        return "USER_" + System.currentTimeMillis() + "_" + random.nextInt(1000);
    }

    /**
     * Validate email format (basic validation)
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Validate password strength
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
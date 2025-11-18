package util;

import java.security.MessageDigest;

public class PasswordUtil {

    
    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    
    public static boolean verifyPassword(String plainPassword, String hashedPassword) throws Exception {
        String newHash = hashPassword(plainPassword);
        return newHash.equals(hashedPassword);
    }
}

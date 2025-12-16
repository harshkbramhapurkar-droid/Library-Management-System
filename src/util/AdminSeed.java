package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminSeed {
    public static void main(String[] args) throws Exception {
        String username = "admin";
        String pass = "admin123";
        String hash = PasswordUtil.hashPassword(pass);
        System.out.println("Hash: " + hash);

        try (Connection conn = DBUtil.getConnection()) {

            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                String sql = "INSERT INTO users(name, email, username, password, role) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, "Administrator");
                stmt.setString(2, "admin@library.com");
                stmt.setString(3, username);
                stmt.setString(4, hash);
                stmt.setString(5, "ADMIN");

                stmt.executeUpdate();
                System.out.println("Admin Created Successfully!");
            } else {
                System.out.println("Admin user already exists. Skipping insert.");
            }
        }
    }
}


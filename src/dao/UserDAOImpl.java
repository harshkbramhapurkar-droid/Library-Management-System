package dao;


import model.User;
import util.DBUtil;
import exception.*;

import java.sql.*;
import java.util.*;

public class UserDAOImpl implements UserDAO {
	
	@Override
	public User findByUsername(String username) throws Exception {
	    String sql = "SELECT * FROM users WHERE username = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, username);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                User user = new User();
	                user.setUserId(rs.getInt("user_id"));
	                user.setUsername(rs.getString("username"));
	                user.setPassword(rs.getString("password")); // note: correct column name
	                user.setFullName(rs.getString("full_name"));
	                user.setRole(rs.getString("role"));
	                user.setCreatedAt(rs.getTimestamp("created_at"));
	                return user;
	            }
	        }
	    }
	    return null; // user not found
	}



    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) throws UserNotFoundException, Exception {
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = extractUser(rs);
                return u;
            } else {
                throw new UserNotFoundException("User lookup failed: usernameOrEmail='" 
                        + usernameOrEmail + "' not found in users table at UserDAOImpl.findByUsernameOrEmail()");
            }
        }
    }

    @Override
    public boolean createUser(User user) throws UserAlreadyExistsException, UserCreationException, Exception {
        if (userExists(user.getUsername()) || userExists(user.getEmail())) {
            throw new UserAlreadyExistsException("User creation failed: username or email already exists (" 
                    + user.getUsername() + ", " + user.getEmail() + ")");
        }

        String sql = "INSERT INTO users (username, email, password, full_name, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getRole());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new UserCreationException("User creation failed at UserDAOImpl.createUser(): " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsersPaginated(int page, int pageSize) throws PaginationException, Exception {
        if (page < 1) throw new PaginationException("Invalid page number: " + page);

        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM users ORDER BY user_id LIMIT ? OFFSET ?";
        List<User> users = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(extractUser(rs));
            }

            if (users.isEmpty()) {
                throw new PaginationException("No users found for page " + page + " (possibly out of range).");
            }

            return users;
        }
    }

    @Override
    public boolean deleteUser(int userId) throws UserNotFoundException, Exception {
        String checkSql = "SELECT user_id FROM users WHERE user_id = ?";
        String delSql = "DELETE FROM users WHERE user_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setInt(1, userId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                throw new UserNotFoundException("User deletion failed: user_id=" + userId + " not found at UserDAOImpl.deleteUser()");
            }

            try (PreparedStatement del = con.prepareStatement(delSql)) {
                del.setInt(1, userId);
                int rows = del.executeUpdate();
                return rows > 0;
            }
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPasswordHash) throws PasswordUpdateException, Exception {
        String sql = "UPDATE users SET password=? WHERE user_id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPasswordHash);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();

            if (rows == 0)
                throw new PasswordUpdateException("Password update failed: user_id=" + userId + " not found at UserDAOImpl.updatePassword()");

            return true;
        } catch (SQLException e) {
            throw new PasswordUpdateException("Password update error in UserDAOImpl.updatePassword(): " + e.getMessage());
        }
    }

    @Override
    public boolean userExists(String usernameOrEmail) throws Exception {
        String sql = "SELECT COUNT(*) FROM users WHERE username=? OR email=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setFullName(rs.getString("full_name"));
        u.setRole(rs.getString("role"));
        return u;
    }


	@Override
	public boolean updatePassword(String username, String newPasswordHash) throws PasswordUpdateException, Exception {
		return false;
	}
}

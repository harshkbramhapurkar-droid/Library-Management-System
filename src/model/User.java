package model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String email;
    
	private String username;
    private String password;
    private String fullName;
    private String role;
    private Timestamp createdAt;
	public int getUserId() {
		return userId;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getFullName() {
		return fullName;
	}
	public String getRole() {
		return role;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
}

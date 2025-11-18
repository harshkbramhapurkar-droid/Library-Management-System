package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import exception.*;
import model.User;
import util.PasswordUtil;

public class UserService {

    private UserDAO userDAO = new UserDAOImpl();

    public User login(String usernameOrEmail, String password)
            throws Exception {

        try {
            User user = userDAO.findByUsername(usernameOrEmail);
            if (user == null) {
                throw new UserNotFoundException("User '" + usernameOrEmail + "' not found in system.");
            }

            if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
                throw new InvalidCredentialsException("Incorrect password for user: " + usernameOrEmail);
            }

            return user;

        } catch (Exception e) {
            if (e instanceof UserNotFoundException || e instanceof InvalidCredentialsException)
                throw e;
            else
                throw new InvalidCredentialsException("Login failed due to unexpected error: " + e.getMessage());
        }
    }

    public boolean register(String username, String fullName, String password)
            throws Exception {

        try {
            User existing = userDAO.findByUsername(username);
            if (existing != null) {
                throw new UserAlreadyExistsException("Username '" + username + "' already taken.");
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFullName(fullName);
            newUser.setPassword(PasswordUtil.hashPassword(password));
            newUser.setRole("USER");

            boolean success = userDAO.createUser(newUser);

            if (!success) {
                throw new UserCreationException("Failed to insert user into database.");
            }

            return true;

        } catch (Exception e) {
            if (e instanceof UserAlreadyExistsException || e instanceof UserCreationException)
                throw e;
            else
                throw new UserCreationException("Registration failed: " + e.getMessage());
        }
    }

    public boolean updatePassword(String username, String newPassword)
            throws PasswordUpdateException {
        try {
            String hashed = PasswordUtil.hashPassword(newPassword);
            return userDAO.updatePassword(username, hashed);
        } catch (Exception e) {
            throw new PasswordUpdateException("Failed to update password for '" + username + "': " + e.getMessage());
        }
    }
}

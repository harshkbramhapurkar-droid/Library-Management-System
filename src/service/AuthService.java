package service;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;
import util.PasswordUtil;

public class AuthService {

    private UserDAO userDAO = new UserDAOImpl();

    public User login(String username, String password) {
        try {
            User user = userDAO.findByUsername(username);

            if (user == null) {
                System.out.println("❌ User not found!");
                return null;
            }

            String hashedInput = PasswordUtil.hashPassword(password);

            if (hashedInput.equals(user.getPassword())) {
                System.out.println("✅ Login Successful! Welcome " + user.getFullName());
                return user;
            } else {
                System.out.println("❌ Incorrect password!");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

package ui;

import java.util.Scanner;

import model.User;
import service.AuthService;
import util.PasswordUtil;

public class LoginUI {
    private Scanner sc = new Scanner(System.in);
    private AuthService authService = new AuthService();

    public void showLoginLoop() {
        boolean running = true;
        while (running) {
            printHeader("LIBRARY MANAGEMENT SYSTEM");
            System.out.println("1) Login");
            System.out.println("2) Register (basic)");
            System.out.println("3) Exit");
            System.out.print("\nEnter choice: ");
            int choice = readInt();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        }
    }

    private void handleLogin() {
        printHeader("LOGIN (username or email allowed)");
        System.out.print("Username or Email: ");
        String id = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        // AuthService expects raw password (it will hash and compare)
        User user = authService.login(id, password);
        if (user == null) {
            System.out.println("\n❌ Login failed. Check credentials.");
            return;
        }

        System.out.println("\n✅ Logged in as: " + user.getFullName() + " [" + user.getRole() + "]");

        // Route to appropriate menu immediately (Q1: A)
        routeToRoleMenu(user);
    }

    private void handleRegister() {
        // Basic registration that uses UserDAOImpl via AuthService or LibraryService can be added.
        // We'll do a simple inline register that calls AuthService -> userDAO create (if implemented).
        printHeader("REGISTER (creates USER role)");
        try {
            System.out.print("Choose username: "); String username = sc.nextLine().trim();
            System.out.print("Full name: "); String fullName = sc.nextLine().trim();
            System.out.print("Password: "); String pass = sc.nextLine().trim();

            model.User u = new model.User();
            u.setUsername(username);
            u.setFullName(fullName);
            u.setPassword(PasswordUtil.hashPassword(pass));
            u.setRole("USER");

            boolean ok = new service.LibraryService().registerUser(u);
            if (ok) System.out.println("✅ Registered successfully. You can login now.");
            else System.out.println("❌ Registration failed (username may already exist).");
        } catch (Exception e) {
            System.out.println("Error while registering: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void routeToRoleMenu(User user) {
        String role = user.getRole() == null ? "USER" : user.getRole().toUpperCase().trim();
        switch (role) {
            case "ADMIN":
                new AdminMenu().showMenu(user);
                break;
            case "LIBRARIAN":
                new LibrarianMenu().showMenu(user);
                break;
            default:
                new UserMenu().showMenu(user);
                break;
        }
    }
    
    private void printHeader(String title) {
        System.out.println("========================================");
        System.out.println("        " + title);
        System.out.println("========================================");
    }

    private int readInt() {
        while (true) {
            try {
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.print("Invalid number. Enter again: ");
            }
        }
    }
}

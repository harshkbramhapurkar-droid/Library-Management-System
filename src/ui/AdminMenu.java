package ui;

import java.util.Scanner;

import model.User;

public class AdminMenu {
    private Scanner sc = new Scanner(System.in);

    public void showMenu(User admin) {
        boolean session = true;
        while (session) {
            printHeader("ADMIN MENU - " + admin.getFullName());
            System.out.println("1) Manage Users (Add / View / Delete / Reset Password)");
            System.out.println("2) Manage Books (View All)");
            System.out.println("3) View Issued Books / Reports");
            System.out.println("4) Logout");
            System.out.print("Choice: ");
            int ch = readInt();
            try {
                switch (ch) {
                    case 1:
                        System.out.println("[Stub] Manage Users - implement DAO/service calls here.");
                        break;
                    case 2:
                        System.out.println("[Stub] Manage Books - implement book management here.");
                        break;
                    case 3:
                        System.out.println("[Stub] Reports - implement report generation here.");
                        break;
                    case 4:
                        session = false;
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void printHeader(String title) {
        System.out.println("----------------------------------------");
        System.out.println("        " + title);
        System.out.println("----------------------------------------");
    }

    private int readInt() {
        while (true) {
            try {
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.print("Invalid. Enter integer: ");
            }
        }
    }
}

package ui;

import java.util.Scanner;

import model.User;

public class UserMenu {
    private Scanner sc = new Scanner(System.in);

    public void showMenu(User user) {
        boolean session = true;
        while (session) {
            printHeader("USER MENU - " + user.getFullName());
            System.out.println("1) Search Books");
            System.out.println("2) List All Books");
            System.out.println("3) Borrow Book");
            System.out.println("4) My Issued History");
            System.out.println("5) Return Book");
            System.out.println("6) Logout");
            System.out.print("Choice: ");
            int ch = readInt();
            try {
                switch (ch) {
                    case 1: System.out.println("[Stub] Search Books - call service.searchBooks()"); break;
                    case 2: System.out.println("[Stub] List Books - call service.listAllBooks()"); break;
                    case 3: System.out.println("[Stub] Borrow Book - call LibraryService.issueBook()"); break;
                    case 4: System.out.println("[Stub] My Issued History - call IssueDAO.getIssuesByUser()"); break;
                    case 5: System.out.println("[Stub] Return Book - call LibraryService.returnBook()"); break;
                    case 6: session = false; break;
                    default: System.out.println("Invalid option");
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

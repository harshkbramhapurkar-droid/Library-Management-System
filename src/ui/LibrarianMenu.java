package ui;

import java.util.Scanner;

import model.User;

public class LibrarianMenu {
    private Scanner sc = new Scanner(System.in);

    public void showMenu(User librarian) {
        boolean session = true;
        while (session) {
            printHeader("LIBRARIAN MENU - " + librarian.getFullName());
            System.out.println("1) Add Book");
            System.out.println("2) Update Book");
            System.out.println("3) Delete Book");
            System.out.println("4) Issue Book to User");
            System.out.println("5) Return Book");
            System.out.println("6) View Issued History");
            System.out.println("7) Logout");
            System.out.print("Choice: ");
            int ch = readInt();
            try {
                switch (ch) {
                    case 1: System.out.println("[Stub] Add Book - call BookDAO/book service."); break;
                    case 2: System.out.println("[Stub] Update Book - implement edit logic."); break;
                    case 3: System.out.println("[Stub] Delete Book - implement delete logic."); break;
                    case 4: System.out.println("[Stub] Issue Book - call LibraryService.issueBook(...)"); break;
                    case 5: System.out.println("[Stub] Return Book - call LibraryService.returnBook(...)"); break;
                    case 6: System.out.println("[Stub] Issued history - show from IssueDAO."); break;
                    case 7: session = false; break;
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

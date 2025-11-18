package controller;

import java.util.List;
import java.util.Scanner;

import model.Book;
import model.IssueRecord;
import model.User;
import service.LibraryService;
import util.PasswordUtil;

public class LibraryController {
    private LibraryService service;
    private Scanner sc = new Scanner(System.in);

    public LibraryController(LibraryService service) {
        this.service = service;
    }

    public void start() {
        System.out.println("=== Library Management System ===");
        boolean run = true;
        while (run) {
            System.out.println("\n1. Login\n2. Register\n3. Exit");
            System.out.print("Choice: ");
            int ch = readInt();
            switch (ch) {
                case 1: loginFlow(); break;
                case 2: registerFlow(); break;
                case 3: run = false; break;
                default: System.out.println("Invalid");
            }
        }
        System.out.println("Bye!");
    }

    private void loginFlow() {
        System.out.print("Username: "); String uname = sc.nextLine().trim();
        System.out.print("Password: "); String pass = sc.nextLine().trim();
        try {
            String hash = PasswordUtil.hashPassword(pass);
            User u = service.login(uname, hash);
            if (u == null) { System.out.println("Invalid credentials"); return; }
            System.out.println("Welcome, " + u.getFullName() + " (" + u.getRole() + ")");
            if ("ADMIN".equalsIgnoreCase(u.getRole())) adminMenu(u);
            else userMenu(u);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void registerFlow() {
        try {
            System.out.print("Username: "); String uname = sc.nextLine().trim();
            System.out.print("Password: "); String pass = sc.nextLine().trim();
            System.out.print("Full name: "); String name = sc.nextLine().trim();
            User u = new User();
            u.setUsername(uname);
            u.setPassword(PasswordUtil.hashPassword(pass));
            u.setFullName(name);
            u.setRole("USER");
            boolean ok = service.registerUser(u);
            System.out.println(ok ? "Registered" : "Registration failed");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void adminMenu(User u) {
        boolean s=true;
        while (s) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1.Add Book 2.Update Book 3.Delete Book 4.List Books 5.Issue Book 6.Return Book 7.All Issues 8.Overdue 9.Logout");
            System.out.print("Choice: "); int ch=readInt();
            try {
                switch (ch) {
                    case 1: addBookFlow(); break;
                    case 2: updateBookFlow(); break;
                    case 3: deleteBookFlow(); break;
                    case 4: listBooks(); break;
                    case 5: issueBookFlow(); break;
                    case 6: returnBookFlow(); break;
                    case 7: allIssuesReport(); break;
                    case 8: overdueReport(); break;
                    case 9: s=false; break;
                    default: System.out.println("Invalid");
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void userMenu(User u) {
        boolean s=true;
        while (s) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1.Search Books 2.List Books 3.Issue Book 4.My Issues 5.Return Book 6.Logout");
            System.out.print("Choice: "); int ch=readInt();
            try {
                switch (ch) {
                    case 1: searchBooksFlow(); break;
                    case 2: listBooks(); break;
                    case 3: issueBookFlowForUser(u); break;
                    case 4: myIssues(u); break;
                    case 5: returnBookFlow(); break;
                    case 6: s=false; break;
                    default: System.out.println("Invalid");
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // flows
    private void addBookFlow() throws Exception {
        Book b = new Book();
        System.out.print("ISBN: "); b.setIsbn(sc.nextLine().trim());
        System.out.print("Title: "); b.setTitle(sc.nextLine().trim());
        System.out.print("Author: "); b.setAuthor(sc.nextLine().trim());
        System.out.print("Publisher: "); b.setPublisher(sc.nextLine().trim());
        System.out.print("Total copies: "); b.setTotalCopies(readInt()); b.setAvailableCopies(b.getTotalCopies());
        System.out.println(service.addBook(b) ? "Book added" : "Add failed");
    }

    private void updateBookFlow() throws Exception {
        System.out.print("Book ID: "); int id = readInt();
        Book b = service.listAllBooks().stream().filter(x->x.getBookId()==id).findFirst().orElse(null);
        if (b==null) { System.out.println("Not found"); return; }
        System.out.print("New title (blank skip): "); String t = sc.nextLine(); if(!t.isEmpty()) b.setTitle(t);
        System.out.print("New author (blank skip): "); String a = sc.nextLine(); if(!a.isEmpty()) b.setAuthor(a);
        System.out.println(service.updateBook(b) ? "Updated" : "Update failed");
    }

    private void deleteBookFlow() throws Exception {
        System.out.print("Book ID: "); int id = readInt();
        System.out.println(service.deleteBook(id) ? "Deleted" : "Delete failed");
    }

    private void listBooks() throws Exception {
        List<Book> list = service.listAllBooks();
        System.out.println("\nBooks:");
        for (Book b : list) {
            System.out.printf("%d | %s | %s | %s | total:%d avail:%d\n",
                    b.getBookId(), b.getIsbn(), b.getTitle(), b.getAuthor(), b.getTotalCopies(), b.getAvailableCopies());
        }
    }

    private void searchBooksFlow() throws Exception {
        System.out.print("Title keyword: "); String kw=sc.nextLine().trim();
        List<Book> list = service.searchBooks(kw);
        System.out.println("Search results:");
        for(Book b:list) System.out.printf("%d | %s | %s | avail:%d\n", b.getBookId(), b.getTitle(), b.getAuthor(), b.getAvailableCopies());
    }

    private void issueBookFlow() throws Exception {
        System.out.print("User ID: "); int uid = readInt();
        System.out.print("Book ID: "); int bid = readInt();
        System.out.println(service.issueBook(uid, bid));
    }

    private void issueBookFlowForUser(User u) throws Exception {
        System.out.print("Book ID: "); int bid = readInt();
        System.out.println(service.issueBook(u.getUserId(), bid));
    }

    private void myIssues(User u) throws Exception {
        List<IssueRecord> list = service.allIssues();
        boolean any=false;
        for (IssueRecord r : list) {
            if (r.getUserId()==u.getUserId()) {
                System.out.printf("IssueID:%d BookID:%d Issue:%s Due:%s Return:%s Fine:%.2f\n",
                   r.getIssueId(), r.getBookId(), r.getIssueDate(), r.getDueDate(), r.getReturnDate(), r.getFine());
                any=true;
            }
        }
        if (!any) System.out.println("No issues");
    }

    private void returnBookFlow() throws Exception {
        System.out.print("Issue ID: "); int iid = readInt();
        System.out.println(service.returnBook(iid));
    }

    private void allIssuesReport() throws Exception {
        List<IssueRecord> list = service.allIssues();
        for (IssueRecord r:list) {
            System.out.printf("IssueID:%d User:%d Book:%d Issue:%s Due:%s Return:%s Fine:%.2f\n",
                    r.getIssueId(), r.getUserId(), r.getBookId(), r.getIssueDate(), r.getDueDate(), r.getReturnDate(), r.getFine());
        }
    }

    private void overdueReport() throws Exception {
        List<IssueRecord> list = service.overdueIssues();
        for (IssueRecord r:list) {
            System.out.printf("IssueID:%d User:%d Book:%d Issue:%s Due:%s\n",
                    r.getIssueId(), r.getUserId(), r.getBookId(), r.getIssueDate(), r.getDueDate());
        }
    }

    private int readInt() {
        while (true) {
            try {
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (Exception e) { System.out.print("Invalid. Enter int: "); }
        }
    }
}

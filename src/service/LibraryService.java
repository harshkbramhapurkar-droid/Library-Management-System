package service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import dao.BookDAO;
import dao.BookDAOImpl;
import dao.IssueDAO;
import dao.IssueDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import model.Book;
import model.IssueRecord;
import model.User;

public class LibraryService {
    private UserDAO userDAO = new UserDAOImpl();
    private BookDAO bookDAO = new BookDAOImpl();
    private IssueDAO issueDAO = new IssueDAOImpl();

    private final int ISSUE_DAYS = 14;
    private final double FINE_PER_DAY = 10.0;

    public User login(String username, String passwordHash) throws Exception {
        User u = userDAO.findByUsername(username);
        if (u != null && u.getPassword().equals(passwordHash)) return u;
        return null;
    }

    public boolean registerUser(User user) throws Exception {
        return userDAO.createUser(user);
    }

    public boolean addBook(Book book) throws Exception {
        book.setAvailableCopies(book.getTotalCopies());
        return bookDAO.addBook(book);
    }

    public boolean updateBook(Book book) throws Exception {
        return bookDAO.updateBook(book);
    }

    public boolean deleteBook(int bookId) throws Exception {
        return bookDAO.deleteBook(bookId);
    }

    public List<Book> searchBooks(String title) throws Exception {
        return bookDAO.searchByTitle(title);
    }

    public List<Book> listAllBooks() throws Exception {
        return bookDAO.getAllBooks();
    }

    public String issueBook(int userId, int bookId) throws Exception {
        Book b = bookDAO.findById(bookId);
        if (b == null) return "Book not found.";
        if (b.getAvailableCopies() <= 0) return "No copies available.";

        List<IssueRecord> curr = issueDAO.getIssuesByUser(userId);
        int active = 0;
        for (IssueRecord r : curr) if (r.getReturnDate() == null) active++;
        if (active >= 3) return "User already has maximum allowed books (3).";

        IssueRecord r = new IssueRecord();
        r.setUserId(userId);
        r.setBookId(bookId);
        LocalDate iss = LocalDate.now();
        r.setIssueDate(Date.valueOf(iss));
        r.setDueDate(Date.valueOf(iss.plusDays(ISSUE_DAYS)));

        boolean ok = issueDAO.issueBook(r);
        if (ok) {
            bookDAO.decrementAvailable(bookId);
            return "Book issued. Due date: " + r.getDueDate();
        }
        return "Issue failed.";
    }

    public String returnBook(int issueId) throws Exception {
        IssueRecord r = issueDAO.findById(issueId);
        if (r == null) return "Issue record not found.";
        if (r.getReturnDate() != null) return "Book already returned.";

        Date today = Date.valueOf(LocalDate.now());
        long daysOverdue = 0;
        if (today.after(r.getDueDate())) {
            daysOverdue = ChronoUnit.DAYS.between(r.getDueDate().toLocalDate(), today.toLocalDate());
        }
        double fine = daysOverdue * FINE_PER_DAY;
        boolean ok = issueDAO.returnBook(issueId, today, fine);
        if (ok) {
            bookDAO.incrementAvailable(r.getBookId());
            return "Book returned. Fine: " + fine;
        }
        return "Return failed.";
    }

    public List<IssueRecord> allIssues() throws Exception {
        return issueDAO.getAllIssues();
    }

    public List<IssueRecord> overdueIssues() throws Exception {
        return issueDAO.getOverdueRecords(Date.valueOf(LocalDate.now()));
    }
}

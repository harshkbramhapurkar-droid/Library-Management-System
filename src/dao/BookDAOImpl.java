package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import util.DBUtil;

public class BookDAOImpl implements BookDAO {
    @Override
    public boolean addBook(Book book) throws Exception {
        String sql = "INSERT INTO books (isbn, title, author, publisher, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, book.getIsbn());
            pst.setString(2, book.getTitle());
            pst.setString(3, book.getAuthor());
            pst.setString(4, book.getPublisher());
            pst.setInt(5, book.getTotalCopies());
            pst.setInt(6, book.getAvailableCopies());
            return pst.executeUpdate() == 1;
        }
    }

    @Override
    public boolean updateBook(Book book) throws Exception {
        String sql = "UPDATE books SET isbn=?, title=?, author=?, publisher=?, total_copies=?, available_copies=? WHERE book_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, book.getIsbn());
            pst.setString(2, book.getTitle());
            pst.setString(3, book.getAuthor());
            pst.setString(4, book.getPublisher());
            pst.setInt(5, book.getTotalCopies());
            pst.setInt(6, book.getAvailableCopies());
            pst.setInt(7, book.getBookId());
            return pst.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteBook(int bookId) throws Exception {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, bookId);
            return pst.executeUpdate() == 1;
        }
    }

    @Override
    public Book findById(int bookId) throws Exception {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, bookId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Book b = new Book();
                    b.setBookId(rs.getInt("book_id"));
                    b.setIsbn(rs.getString("isbn"));
                    b.setTitle(rs.getString("title"));
                    b.setAuthor(rs.getString("author"));
                    b.setPublisher(rs.getString("publisher"));
                    b.setTotalCopies(rs.getInt("total_copies"));
                    b.setAvailableCopies(rs.getInt("available_copies"));
                    return b;
                }
            }
        }
        return null;
    }

    @Override
    public List<Book> searchByTitle(String title) throws Exception {
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        List<Book> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + title + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Book b = new Book();
                    b.setBookId(rs.getInt("book_id"));
                    b.setIsbn(rs.getString("isbn"));
                    b.setTitle(rs.getString("title"));
                    b.setAuthor(rs.getString("author"));
                    b.setPublisher(rs.getString("publisher"));
                    b.setTotalCopies(rs.getInt("total_copies"));
                    b.setAvailableCopies(rs.getInt("available_copies"));
                    list.add(b);
                }
            }
        }
        return list;
    }

    @Override
    public List<Book> getAllBooks() throws Exception {
        String sql = "SELECT * FROM books";
        List<Book> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Book b = new Book();
                b.setBookId(rs.getInt("book_id"));
                b.setIsbn(rs.getString("isbn"));
                b.setTitle(rs.getString("title"));
                b.setAuthor(rs.getString("author"));
                b.setPublisher(rs.getString("publisher"));
                b.setTotalCopies(rs.getInt("total_copies"));
                b.setAvailableCopies(rs.getInt("available_copies"));
                list.add(b);
            }
        }
        return list;
    }

    @Override
    public boolean decrementAvailable(int bookId) throws Exception {
        String sql = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, bookId);
            return pst.executeUpdate() == 1;
        }
    }

    @Override
    public boolean incrementAvailable(int bookId) throws Exception {
        String sql = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, bookId);
            return pst.executeUpdate() == 1;
        }
    }
}

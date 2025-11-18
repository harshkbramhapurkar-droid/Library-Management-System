package dao;

import java.util.List;
import model.Book;

public interface BookDAO {
    boolean addBook(Book book) throws Exception;
    boolean updateBook(Book book) throws Exception;
    boolean deleteBook(int bookId) throws Exception;
    Book findById(int bookId) throws Exception;
    List<Book> searchByTitle(String title) throws Exception;
    List<Book> getAllBooks() throws Exception;
    boolean decrementAvailable(int bookId) throws Exception;
    boolean incrementAvailable(int bookId) throws Exception;
}

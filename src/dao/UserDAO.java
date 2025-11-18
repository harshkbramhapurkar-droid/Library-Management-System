package dao;

import java.util.List;
import model.User;
import exception.*;

public interface UserDAO {
    User findByUsernameOrEmail(String usernameOrEmail) throws UserNotFoundException, Exception;
    boolean createUser(User user) throws UserAlreadyExistsException, UserCreationException, Exception;
    List<User> getAllUsersPaginated(int page, int pageSize) throws PaginationException, Exception;
    boolean deleteUser(int userId) throws UserNotFoundException, Exception;
    boolean updatePassword(String username, String newPasswordHash) throws PasswordUpdateException, Exception;
    boolean userExists(String usernameOrEmail) throws Exception;
	User findByUsername(String username) throws Exception;
	boolean updatePassword(int userId, String newPasswordHash) throws PasswordUpdateException, Exception;
}

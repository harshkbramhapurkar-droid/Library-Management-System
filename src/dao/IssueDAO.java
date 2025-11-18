package dao;

import java.util.List;
import model.IssueRecord;

public interface IssueDAO {
    boolean issueBook(IssueRecord record) throws Exception;
    boolean returnBook(int issueId, java.sql.Date returnDate, double fine) throws Exception;
    List<IssueRecord> getIssuesByUser(int userId) throws Exception;
    List<IssueRecord> getAllIssues() throws Exception;
    IssueRecord findById(int issueId) throws Exception;
    List<IssueRecord> getOverdueRecords(java.sql.Date today) throws Exception;
}

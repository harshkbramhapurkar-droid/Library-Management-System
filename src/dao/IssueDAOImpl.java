package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.IssueRecord;
import util.DBUtil;

public class IssueDAOImpl implements IssueDAO {
    @Override
    public boolean issueBook(IssueRecord r) throws Exception {
        String sql = "INSERT INTO issues (user_id, book_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, r.getUserId());
            pst.setInt(2, r.getBookId());
            pst.setDate(3, r.getIssueDate());
            pst.setDate(4, r.getDueDate());
            return pst.executeUpdate() == 1;
        }
    }

    @Override
    public boolean returnBook(int issueId, Date returnDate, double fine) throws Exception {
        String sql = "UPDATE issues SET return_date = ?, fine = ? WHERE issue_id = ? AND return_date IS NULL";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDate(1, returnDate);
            pst.setDouble(2, fine);
            pst.setInt(3, issueId);
            return pst.executeUpdate() == 1;
        }
    }

    @Override
    public List<IssueRecord> getIssuesByUser(int userId) throws Exception {
        String sql = "SELECT * FROM issues WHERE user_id = ?";
        List<IssueRecord> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<IssueRecord> getAllIssues() throws Exception {
        String sql = "SELECT * FROM issues";
        List<IssueRecord> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public IssueRecord findById(int issueId) throws Exception {
        String sql = "SELECT * FROM issues WHERE issue_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, issueId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<IssueRecord> getOverdueRecords(Date today) throws Exception {
        String sql = "SELECT * FROM issues WHERE due_date < ? AND return_date IS NULL";
        List<IssueRecord> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setDate(1, today);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    private IssueRecord mapRow(ResultSet rs) throws Exception {
        IssueRecord r = new IssueRecord();
        r.setIssueId(rs.getInt("issue_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setBookId(rs.getInt("book_id"));
        r.setIssueDate(rs.getDate("issue_date"));
        r.setDueDate(rs.getDate("due_date"));
        r.setReturnDate(rs.getDate("return_date"));
        r.setFine(rs.getDouble("fine"));
        return r;
    }
}

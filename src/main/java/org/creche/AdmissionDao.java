package org.creche;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdmissionDao {
    private Connection conn;

    public AdmissionDao(Connection conn) {
        this.conn = conn;
    }

    public void save(Admission admission) throws SQLException {
        String sql = "INSERT INTO admissions(user_id, child_id, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, admission.getUserId());
            ps.setInt(2, admission.getChildId());
            ps.setString(3, admission.getStatus());
       
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                admission.setId(rs.getInt(1));
            }
        }
    }

    /* public List<Admission> findAll() throws SQLException {
        List<Admission> admissions = new ArrayList<>();
        String sql = "SELECT * FROM admissions";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Admission a = new Admission();
            a.setId(rs.getInt("id"));
            a.setUserId(rs.getInt("user_id"));
            a.setStatus(rs.getString("status"));
            a.setRequestedAt(rs.getTimestamp("requested_at"));
            admissions.add(a);
        }
        stmt.close();
        return admissions;
    } */

    public List<Map<String, Object>> findAllWithDetails() throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = """
                    SELECT a.id AS admission_id, a.status, a.requested_at,
                           u.username AS parent_username,
                           c.name AS child_name, c.birthdate, c.allergies, c.special_needs
                    FROM admissions a
                    JOIN users u ON a.user_id = u.id
                    JOIN children c ON a.child_id = c.id
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getInt("admission_id"));
                row.put("status", rs.getString("status"));
                row.put("requested_at", rs.getTimestamp("requested_at").toString());
                row.put("parent_username", rs.getString("parent_username"));
                row.put("child_name", rs.getString("child_name"));
                row.put("birthdate", rs.getDate("birthdate").toString());
                row.put("allergies", rs.getString("allergies"));
                row.put("special_needs", rs.getString("special_needs"));
                result.add(row);
            }
        }
        return result;
    }

    public void updateStatus(int admissionId, String status) throws SQLException {
        String sql = "UPDATE admissions SET status = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ps.setInt(2, admissionId);
        ps.executeUpdate();
        ps.close();
    }
}

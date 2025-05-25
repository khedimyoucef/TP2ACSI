package org.creche;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdmissionDao {
    private Connection conn;

    public AdmissionDao(Connection conn) {
        this.conn = conn;
    }

    public void save(Admission admission) throws SQLException {
        String sql = "INSERT INTO admissions(user_id, status) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, admission.getUserId());
        ps.setString(2, admission.getStatus());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            admission.setId(rs.getInt(1));
        }
        ps.close();
    }

    public List<Admission> findAll() throws SQLException {
        List<Admission> admissions = new ArrayList<>();
        String sql = "SELECT * FROM admissions";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Admission a = new Admission();
            a.setId(rs.getInt("id"));
            a.setUserId(rs.getInt("user_id"));
            a.setStatus(rs.getString("status"));
            admissions.add(a);
        }
        stmt.close();
        return admissions;
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

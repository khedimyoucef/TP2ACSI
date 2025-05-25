package org.creche;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChildDao {
    private Connection conn;

    public ChildDao(Connection conn) {
        this.conn = conn;
    }

    public void save(Child child) throws SQLException {
        String sql = "INSERT INTO children(user_id, name, birthdate, allergies, special_needs) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, child.getUserId());
        ps.setString(2, child.getName());
        ps.setString(3, child.getBirthdate());
        ps.setString(4, child.getAllergies());
        ps.setString(5, child.getSpecialNeeds());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            child.setId(rs.getInt(1));
        }
        ps.close();
    }

    public List<Child> findByUserId(int userId) throws SQLException {
        List<Child> children = new ArrayList<>();
        String sql = "SELECT * FROM children WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Child c = new Child();
            c.setId(rs.getInt("id"));
            c.setUserId(rs.getInt("user_id"));
            c.setName(rs.getString("name"));
            c.setBirthdate(rs.getString("birthdate"));
            c.setAllergies(rs.getString("allergies"));
            c.setSpecialNeeds(rs.getString("special_needs"));
            children.add(c);
        }
        ps.close();
        return children;
    }

    

}

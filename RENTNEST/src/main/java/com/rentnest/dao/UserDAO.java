package com.rentnest.dao;

import com.rentnest.models.user;
import com.rentnest.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean registerUser(String username, String email, String passwordHash) {
        String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // log or rethrow as your app-level exception
            e.printStackTrace();
            return false;
        }
    }

    public user loginUser(String email, String passwordHash) {
        String sql = "SELECT id, username, email, password_hash FROM users WHERE email = ? AND password_hash = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new user(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password_hash")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<user> getAllUsers() {
        String sql = "SELECT id, username, email, password_hash FROM users";
        List<user> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new user(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

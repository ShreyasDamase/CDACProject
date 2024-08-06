package com.controller.dao;

import com.controller.pojo.User;
import com.controller.utils.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Create a new user record
    public boolean addUser(User user) {
//        String sql = "INSERT INTO users (userId, username, password, email, firstName, lastName, address, phoneNumber, role, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	String sql = "INSERT INTO Users (user_id,username, email, role) VALUES (?,?,?,?)";
        User temp = getUserById(user.getUserId());
        if(temp==null) {

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getEmail());

            stmt.setString(3, user.getEmail());

            stmt.setString(4, user.getRole());
//            stmt.setString(4, user.getEmail());
//            stmt.setString(5, user.getFirstName());
//            stmt.setString(6, user.getLastName());
//            stmt.setString(7, user.getAddress());
//            stmt.setString(8, user.getPhoneNumber());
//            stmt.setString(9, user.getRole());
//            stmt.setTimestamp(10, user.getCreatedAt());
//            stmt.setTimestamp(11, user.getUpdatedAt());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
        }
        return false;
    }

    // Retrieve a user by its ID
    public User getUserById(String userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        User user = null;

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getString("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setAddress(rs.getString("address"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setRole(rs.getString("role"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Retrieve all users
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM Users";
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("userId"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setAddress(rs.getString("address"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("createdAt"));
                user.setUpdatedAt(rs.getTimestamp("updatedAt"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Update a user record
    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET username = ?, email = ?, first_name = ?, last_name = ?, address = ?, phone_number = ?, role = ?, created_at = ?, updated_at = ? WHERE user_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getRole());
            stmt.setTimestamp(8, user.getCreatedAt());
            stmt.setTimestamp(9, user.getUpdatedAt());
            stmt.setString(10, user.getUserId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
    }

    // Delete a user record
    public void deleteUser(String userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

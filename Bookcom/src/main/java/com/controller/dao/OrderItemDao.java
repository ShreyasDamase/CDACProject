package com.controller.dao;

import com.controller.pojo.OrderItem;
import com.controller.utils.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDao {

    // Create a new order item record
    public void addOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (orderId, bookId, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getBookId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setBigDecimal(4, orderItem.getPrice());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve an order item by its ID
    public OrderItem getOrderItemById(int orderItemId) {
        String sql = "SELECT * FROM order_items WHERE orderItemId = ?";
        OrderItem orderItem = null;

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItemId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    orderItem = new OrderItem();
                    orderItem.setOrderItemId(rs.getInt("orderItemId"));
                    orderItem.setOrderId(rs.getInt("orderId"));
                    orderItem.setBookId(rs.getInt("bookId"));
                    orderItem.setQuantity(rs.getInt("quantity"));
                    orderItem.setPrice(rs.getBigDecimal("price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItem;
    }

    // Retrieve all order items
    public List<OrderItem> getAllOrderItems() {
        String sql = "SELECT * FROM order_items";
        List<OrderItem> orderItems = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderItemId(rs.getInt("orderItemId"));
                orderItem.setOrderId(rs.getInt("orderId"));
                orderItem.setBookId(rs.getInt("bookId"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setPrice(rs.getBigDecimal("price"));

                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    // Update an order item record
    public void updateOrderItem(OrderItem orderItem) {
        String sql = "UPDATE order_items SET orderId = ?, bookId = ?, quantity = ?, price = ? WHERE orderItemId = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getBookId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setBigDecimal(4, orderItem.getPrice());
            stmt.setInt(5, orderItem.getOrderItemId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an order item record
    public void deleteOrderItem(int orderItemId) {
        String sql = "DELETE FROM order_items WHERE orderItemId = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

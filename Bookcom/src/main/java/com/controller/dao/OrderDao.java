package com.controller.dao;

import com.controller.pojo.Order;
import com.controller.utils.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    // Create a new order record
    public void addOrder(Order order) {
        String sql = "INSERT INTO Orders (buyer_id, book_id, total_amount, order_status, delivery_date, shipping_address, payment_method, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getBuyerId());
            stmt.setInt(2, order.getBookId()); // Set book_id
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setString(4, order.getOrderStatus());
//            stmt.setTimestamp(5, order.getOrderDate());
            stmt.setTimestamp(5, order.getDeliveryDate());
            stmt.setString(6, order.getShippingAddress());
            stmt.setString(7, order.getPaymentMethod());
            stmt.setString(8, order.getPaymentStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve an order by its ID
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        Order order = null;

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setBuyerId(rs.getString("buyer_id"));
                    order.setBookId(rs.getInt("book_id")); // Get book_id
                    order.setTotalAmount(rs.getBigDecimal("total_amount"));
                    order.setOrderStatus(rs.getString("order_status"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setDeliveryDate(rs.getTimestamp("delivery_date"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setPaymentStatus(rs.getString("payment_status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    // Retrieve all orders
    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM Orders";
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setBuyerId(rs.getString("buyer_id"));
                order.setBookId(rs.getInt("book_id")); // Get book_id
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setOrderStatus(rs.getString("order_status"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setDeliveryDate(rs.getTimestamp("delivery_date"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setPaymentStatus(rs.getString("payment_status"));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Update an order record
    public void updateOrder(Order order) {
        String sql = "UPDATE Orders SET buyer_id = ?, book_id = ?, total_amount = ?, order_status = ?, order_date = ?, delivery_date = ?, shipping_address = ?, payment_method = ?, payment_status = ? WHERE order_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getBuyerId());
            stmt.setInt(2, order.getBookId()); // Set book_id
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setString(4, order.getOrderStatus());
            System.out.println(new Timestamp(System.currentTimeMillis()));
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            stmt.setTimestamp(6, order.getDeliveryDate());
            stmt.setString(7, order.getShippingAddress());
            stmt.setString(8, order.getPaymentMethod());
            stmt.setString(9, order.getPaymentStatus());
            stmt.setInt(10, order.getOrderId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an order record
    public void deleteOrder(int orderId) {
    	System.out.println(orderId);
        String sql = "DELETE FROM Orders WHERE order_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteOrderByBookId(int orderId) {
    	System.out.println(orderId);
        String sql = "DELETE FROM Orders WHERE book_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

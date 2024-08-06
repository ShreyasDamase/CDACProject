package com.controller.dao;

import com.controller.pojo.Book;
import com.controller.utils.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    // Create a new book record
	 public int addBook(Book book) {
	        String sql = "INSERT INTO Books (title, author, isbn, publisher, publication_year, category_id, price, description, cover_image_url, seller_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        int bookId = -1;

	        try (Connection conn = DBConfig.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            stmt.setString(1, book.getTitle());
	            stmt.setString(2, book.getAuthor());
	            stmt.setString(3, book.getIsbn());
	            stmt.setString(4, book.getPublisher());
	            stmt.setString(5, book.getPublicationYear());
	            stmt.setInt(6, book.getCategoryId());
	            stmt.setBigDecimal(7, book.getPrice());
	            stmt.setString(8, book.getDescription());
	            stmt.setString(9, book.getCoverImageUrl());
	            stmt.setString(10, book.getSellerId());
	            stmt.setTimestamp(11, book.getCreatedAt());
	            stmt.setTimestamp(12, book.getUpdatedAt());

	            int affectedRows = stmt.executeUpdate();

	            if (affectedRows > 0) {
	                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        bookId = generatedKeys.getInt(1);
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return bookId;
	    }

    // Retrieve a book by its ID
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM Books WHERE book_id = ?";
        Book book = null;

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublisher(rs.getString("publisher"));
                    book.setPublicationYear(rs.getString("publication_year"));
                    book.setCategoryId(rs.getInt("category_id"));
                    book.setPrice(rs.getBigDecimal("price"));
                    book.setDescription(rs.getString("description"));
                    book.setCoverImageUrl(rs.getString("cover_image_url"));
                    book.setSellerId(rs.getString("seller_id"));
                    book.setCreatedAt(rs.getTimestamp("created_at"));
                    book.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
                System.out.println(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    // Retrieve all books
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM Books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublicationYear(rs.getString("publication_year"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setPrice(rs.getBigDecimal("price"));
                book.setDescription(rs.getString("description"));
                book.setCoverImageUrl(rs.getString("cover_image_url"));
                book.setSellerId(rs.getString("seller_id"));
                book.setCreatedAt(rs.getTimestamp("created_at"));
                book.setUpdatedAt(rs.getTimestamp("updated_at"));

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Update a book record
    public void updateBook(Book book) {
        String sql = "UPDATE Books SET title = ?, author = ?, isbn = ?, publisher = ?, publication_year = ?, category_id = ?, price = ?, description = ?, cover_image_url = ?, seller_id = ?, created_at = ?, updated_at = ? WHERE book_id = ?";
        System.out.println(book.getCoverImageUrl());
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getPublisher());
            stmt.setString(5, book.getPublicationYear());
            stmt.setInt(6, book.getCategoryId());
            stmt.setBigDecimal(7, book.getPrice());
            stmt.setString(8, book.getDescription());
            stmt.setString(9, book.getCoverImageUrl());
            stmt.setString(10, book.getSellerId());
            stmt.setTimestamp(11, book.getCreatedAt());
            stmt.setTimestamp(12, book.getUpdatedAt());
            stmt.setInt(13, book.getBookId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a book record
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM Books WHERE book_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

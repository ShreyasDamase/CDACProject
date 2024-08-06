package com.controller.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
	public static Connection getConnection() {
		Connection getConnection=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			 getConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_selling_platform","root","bokya");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getConnection;	
	}
}

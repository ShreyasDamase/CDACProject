package com.controller.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controller.dao.BookDao;
import com.controller.dao.CategoryDao;
import com.controller.dao.OrderDao;
import com.controller.dao.UserDao;
import com.controller.pojo.Book;
import com.controller.pojo.Order;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDao order=null;
    private UserDao user=null;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Implement GET method to retrieve order information
    	switch(request.getPathInfo()) {
    	case "/getBooks":
    		getAllBooks(request,response);
    		break;
    	}
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	switch(request.getPathInfo()) {
    	case "/create":
    		createOrder(request,response);
    		break;
    	}
        // Implement POST method to create a new order
    
    	
    }
    private void getAllBooks(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    	try {
    	order=new OrderDao();
    	List<Order> orders=order.getAllOrders();
    	List<List> mainList=new ArrayList<List>();
    	for(Order i:orders) {
    		
    		if(i.getBuyerId().equals(request.getAttribute("sub"))){
    		List<String> object=new ArrayList<String>();
    		Book book= new BookDao().getBookById(i.getBookId());
    		object.add(Integer.toString(i.getBookId()));
    		object.add(book.getAuthor());
    		object.add( book.getTitle());
    		object.add(new CategoryDao().getCategoryById(book.getCategoryId()).getCategoryName());
    		object.add( book.getPublisher());
    		object.add( book.getPrice().toString());
    		mainList.add(object);
    		
    		}
    	}
    	Gson gson = new Gson();
        String json = gson.toJson(mainList);

        // Send JSON response
        response.setContentType("application/json");
        response.getWriter().write(json);
    	}
    	catch(Exception e) {
    		 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
             response.getWriter().write("Failed to get the book");
    	}
    }
    private void createOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
    	String userid=request.getAttribute("sub").toString();
    	JsonObject object= new JsonObject();
    	StringBuilder jsonBuffer= new StringBuilder();
    	BufferedReader jsonreader= request.getReader();
    	String line;
    	while((line=jsonreader.readLine())!=null) {
    		
    		jsonBuffer.append(line);
    	}
    	JsonParser parser=new JsonParser();
//    	System.out.println(jsonBuffer);
    	object=parser.parse(jsonBuffer.toString()).getAsJsonObject();
//    	System.out.println(object);
    	

        String status = null;
        try {
            // acquire a secure SMTPs session
            Properties pros = new Properties();
            pros.put("mail.transport.protocol", "smtps");
            pros.put("mail.smtps.host", "smtp.gmail.com");
            pros.put("mail.smtps.port", 465);
            pros.put("mail.smtps.auth", "true");
            pros.put("mail.smtps.quitwait", "false");
            Session session
                = Session.getDefaultInstance(pros);
            session.setDebug(true);
            // Wrap a message in session
            Message message = new MimeMessage(session);
            message.setSubject("Hello world");
            object.addProperty("buyer", userid);
            object.addProperty("phone", new UserDao().getUserById(request.getAttribute("sub").toString()).getPhoneNumber());
            String body=object.toString();
            if (true) {
                message.setContent(body, "text/html");
            }
            else {
                message.setText(body);
            }
            // specify E-mail address of Sender and Receiver
            Address sender = new InternetAddress("omkarhalgi90@gmail.com");
            Address receiver = new InternetAddress("omkarhalgi50@gmail.com");
            message.setFrom(sender);
            message.setRecipient(Message.RecipientType.TO,
                                 receiver);
            // sending an E-mail
            try (Transport tt = session.getTransport()) {
                // acqruiring a connection to remote server
                tt.connect("bookcomproject@gmail.com", "whlh twwg gpym qcgo");
                tt.sendMessage(message,
                               message.getAllRecipients());
                status = "E-Mail Sent Successfully";
                System.out.println("One have asdlfksafkdk");
//                System.out.println(object.get("book_id"));
                Book current_book= new BookDao().getBookById(Integer.parseInt(object.get("bookid").getAsString()));
                Order new_order= new Order();
                new_order.setBookId(current_book.getBookId());
                new_order.setBuyerId(userid);
                new_order.setOrderStatus("Pending");
                new_order.setPaymentMethod("Offline");
                new_order.setPaymentStatus("Processing");
                new_order.setTotalAmount(current_book.getPrice());
                new OrderDao().addOrder(new_order);
            }
        }
        catch (MessagingException e) {
            status = e.toString();
        }
//        catch (UnsupportedEncodingException e) {
//            status = e.toString();
//        }
        // return the status of email
        
        response.getWriter().write(object.toString());
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Implement PUT method to update order information
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Implement DELETE method to delete an order
    	new OrderDao().deleteOrderByBookId(Integer.parseInt(request.getParameter("id")));
    	response.getWriter().print("Order Deleted");
    	
    }
}


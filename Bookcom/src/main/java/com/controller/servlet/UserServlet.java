package com.controller.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controller.dao.UserDao;
import com.controller.pojo.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**	
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDao user;
    /**
     * @see HttpServlet#HttpServlet()
     */
	public void init() {
		user=new UserDao();
	}
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println(request.getPathInfo());
		switch(request.getPathInfo()){
			case "/details":
				userDetails(request,response);
				break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		System.out.println(request.getAttribute("email"));
//		System.out.println(request.getAttribute("sub"));

		// TODO Auto-generated method stub
//		System.out.println("slkf");
//		System.out.println(request.getPathInfo());
//		System.out.println(request.getServletPath());
//		System.out.println(request.getParameter("email"));
		switch(request.getPathInfo()) {
		case "/register":
			
			registerSuccess(request,response);
			break ;
		case "/upload":
			uploadImage(request,response);
			break;
		case "/update":
			updateUser(request,response);
			break;
		default :
			break;	
		}
	}
	protected void uploadImage(HttpServletRequest request,HttpServletResponse response) throws IllegalStateException, IOException, ServletException {
//		System.out.println("Upload file");        
				Part filePart = request.getPart("file");
		        String fileName = getFileName(filePart);

		        String uploadPath = "path/to/upload/directory"; // Replace with your desired upload directory path
		        File uploadDir = new File(uploadPath);
		        if (!uploadDir.exists()) {
		            uploadDir.mkdirs();
		        }

		        try (InputStream fileContent = filePart.getInputStream();
		             FileOutputStream fos = new FileOutputStream(new File(uploadPath + File.separator + fileName))) {
		            int read;
		            final byte[] bytes = new byte[1024];
		            while ((read = fileContent.read(bytes)) != -1) {
		                fos.write(bytes, 0, read);
		            }
		            response.getWriter().println("File uploaded successfully!");
		        } catch (Exception e) {
		            response.getWriter().println("File upload failed: " + e.getMessage());
		        }
		    }

		    private String getFileName(Part part) {
		        for (String content : part.getHeader("content-disposition").split(";")) {
		            if (content.trim().startsWith("filename")) {
		                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
		            }
		        }
		        return null;
	}
	protected void registerSuccess(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
			 response.setContentType("application/json");
		        response.setCharacterEncoding("UTF-8");
		        JsonObject jsonResponse = new JsonObject();
	        
	        try {
	            // Create new user and set its attributes from the request
	        	
	            User new_user = new User();
	            new_user.setUserId(request.getAttribute("sub").toString());
	            new_user.setEmail(request.getAttribute("email").toString());
	            
	            new_user.setRole("user");
	            
	            // Add user to the database or user storage
	            boolean isAdded = user.addUser(new_user);  // Assuming addUser returns a boolean

	            if (isAdded) {
	                // If the user is added successfully, send success response
	                jsonResponse.addProperty("message", "User added successfully.");
	                jsonResponse.addProperty("status", "success");
	                response.setStatus(HttpServletResponse.SC_OK);
	            }
	            else {
	                // If the user is not added, send an error response
	                jsonResponse.addProperty("message", "User already created");
	                jsonResponse.addProperty("status", "success");
	                response.setStatus(HttpServletResponse.SC_CREATED);
	            }
	        } catch (Exception e) {
	            // Handle any exceptions that occur and send an error response
	            jsonResponse.addProperty("message", "An error occurred: " + e.getMessage());
	            jsonResponse.addProperty("status", "error");
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }

	        // Write the JSON response
	        PrintWriter out = response.getWriter();
	        out.print(jsonResponse.toString());
	        out.flush();
	}
	protected void userDetails(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		 response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        JsonObject jsonResponse = new JsonObject();
		String userid=(String)request.getAttribute("sub");
		System.out.println("Print user id");
		User client=user.getUserById(userid);
		jsonResponse.addProperty("email",client.getEmail());
		jsonResponse.addProperty("username",client.getUsername());
		jsonResponse.addProperty("fname",client.getFirstName());
		jsonResponse.addProperty("lname",client.getLastName());
		jsonResponse.addProperty("address",client.getAddress());
		jsonResponse.addProperty("number",client.getPhoneNumber());
		response.getWriter().print(jsonResponse.toString());
		
		
	}
	protected void updateUser(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JsonObject jsonResponse = new JsonObject();
		  StringBuilder jsonBuffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuffer.append(line);
        }

        // Convert the JSON data to a JsonObject
        String jsonString = jsonBuffer.toString();
        JsonParser parser=new JsonParser();
        JsonObject jsonobject=parser.parse(jsonString).getAsJsonObject();
        System.out.println(jsonobject);
		// Get user ID from request
		String userid = (String) request.getAttribute("sub");

		// Get user information from request parameters
		String username = jsonobject.get("username").getAsString();
		String fname = jsonobject.get("fname").getAsString();
		String lname = jsonobject.get("lname").getAsString();
		String email = jsonobject.get("email").getAsString();
		String number = jsonobject.get("number").getAsString();
		String address = jsonobject.get("address").getAsString();
		// Get user from database
		User client = user.getUserById(userid);
		
		// If user exists, update user information
		if (client != null) {
		    client.setUsername(username);
		    client.setFirstName(fname);
		    client.setLastName(lname);
		    client.setEmail(email);
		    client.setPhoneNumber(number);
		    client.setAddress(address);

		    // Update user in the database
		    boolean updateUser = user.updateUser(client);

		    // Check if user update was successful
		    if (updateUser) {
		        // Send success message in JSON response
		        jsonResponse.addProperty("message", "User updated successfully");
		    } else {
		        // Send error message in JSON response if update failed
		        jsonResponse.addProperty("message", "Failed to update user");
		    }
		} else {
		    // Send error message in JSON response if user does not exist
		    jsonResponse.addProperty("message", "User not found");
		}

		// Write JSON response to the output stream
		response.getWriter().print(jsonResponse.toString());

		
		
	}
	
}


//	        System.out.println(jsonobject.get("userid"));
//	        Cookie[] cookies = request.getCookies();

//	        JsonObject cookiesJson = new JsonObject();
//	        if (cookies != null) {
//	            for (Cookie cookie : cookies) {
//	                cookiesJson.addProperty(cookie.getName(), cookie.getValue());
//	            }
//	        }
//
//	        // Add the cookies to the JSON response
//	        jsonobject.add("cookies", request.getAttributeNames());
//	        // Send the JSON object as response
//		String password=req.getParameter("password");

//		user.insertUser(email, password);
//		ArrayList<Integer> lst=new ArrayList<Integer>();
//		lst.add(123);
//		lst.add(23);
//		lst.add(23);
//		String json= new Gson().toJson(lst);
//		res.getWriter().append(json);
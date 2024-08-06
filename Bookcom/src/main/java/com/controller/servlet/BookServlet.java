package com.controller.servlet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;

import com.controller.dao.BookDao;
import com.controller.dao.CategoryDao;
import com.controller.dao.UserDao;
import com.controller.pojo.Book;
import com.controller.pojo.Category;
import com.controller.pojo.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
@MultipartConfig
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDao bookDao=null;
    private CategoryDao category=null;
    @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	bookDao= new BookDao();
    	category= new CategoryDao();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	System.out.println(request.getPathInfo());
    	switch(request.getPathInfo()) {
    	case "/getCategory":
    		getCategory(request,response);
    		break;
    	case "/getImage":
    		getImage(request,response);
    		break;
    	case "/getAllImages":
    		getAllImages(request,response);
    		break;
    	}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch(request.getPathInfo()) {
		case "/create":
			createBook(request,response);
			break;
		case "/update":
			updateBook(request,response);
			break;
		case "/getDescription":
			getDescription(request, response);
			break;
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
    private void getImage(HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException, IOException {
    	  String subDirectory = (String) request.getAttribute("sub");
    	  String DIRECTORY_PATH = "D:/Webprograming/CDAC project/images/" + subDirectory;
          File directory = new File(DIRECTORY_PATH);
          List<List> imageDataList = new ArrayList<>();

          if (directory.exists() && directory.isDirectory()) {
              for (File subDir : directory.listFiles()) {
            	  List<String> templist=new ArrayList<String>();
            	  templist.add(subDir.getName());
            	  File front= new File(subDir+"/front");
                  if (subDir.isDirectory()) {
                      for (File file : front.listFiles()) {
//                	  File directory = new File()
                          if (file.isFile()) {
                              try (FileInputStream fis = new FileInputStream(file)) {
                                  byte[] bytes = new byte[(int) file.length()];
                                  fis.read(bytes);
                                  String base64Image = Base64.getEncoder().encodeToString(bytes);
                                  String imageData = "data:image/jpeg;base64," + base64Image;
                                  templist.add(imageData);
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                          }
                      }
                  }
                  File back= new File(subDir+"/back");
                  if (subDir.isDirectory()) {
                      for (File file : back.listFiles()) {
//                	  File directory = new File()
                          if (file.isFile()) {
                              try (FileInputStream fis = new FileInputStream(file)) {
                                  byte[] bytes = new byte[(int) file.length()];
                                  fis.read(bytes);
                                  String base64Image = Base64.getEncoder().encodeToString(bytes);
                                  String imageData = "data:image/jpeg;base64," + base64Image;
                                  templist.add(imageData);
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                          }
                      }
                  }
                  imageDataList.add(templist);
              }
          }

          // Convert list to JSON
          Gson gson = new Gson();
          String json = gson.toJson(imageDataList);

          // Send JSON response
          response.setContentType("application/json");
          response.getWriter().write(json);
    }
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Implement PUT method to update book information
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
//    	System.out.println("Delete");
    	try {
    	int storeid=Integer.parseInt(request.getParameter("id").toString());
    	bookDao.deleteBook(storeid);
    	response.getWriter().println("Update Request processed successfully");
    	 String directoryPath = "D:/Webprograming/CDAC project/images/"+request.getAttribute("sub").toString()+"/"+storeid;

         if (directoryPath == null || directoryPath.isEmpty()) {
             response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
             response.getWriter().write("Directory path is missing");
             return;
         }

         // Define the base directory where directories are stored
         String baseDirectory = "/path/to/your/base/directory/";

         // Construct the full directory path
         File directory = new File(directoryPath);

         if (directory.exists() && directory.isDirectory()) {
             try {
                 // Attempt to delete the directory and its contents using Apache Commons IO
                 FileUtils.deleteDirectory(directory);
                 response.setStatus(HttpServletResponse.SC_OK);
                 response.getWriter().write("Directory deleted successfully");
             } catch (IOException e) {
                 response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                 response.getWriter().write("Failed to delete the directory");
                 e.printStackTrace();
             }
         } else {
             response.setStatus(HttpServletResponse.SC_NOT_FOUND);
             response.getWriter().write("Directory not found");
         }
    	}
    	catch(Exception E){
    		System.out.println(E.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Error processing request");

    	}
//    	response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
//		JsonObject jsonResponse = new JsonObject();
//		  StringBuilder jsonBuffer = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            jsonBuffer.append(line);
//        }
//
//        // Convert the JSON data to a JsonObject
//        String jsonString = jsonBuffer.toString();
//        JsonParser parser=new JsonParser();
//        JsonObject jsonobject=parser.parse(jsonString).getAsJsonObject();
//        System.out.println(jsonobject);
        // Implement DELETE method to delete a book
    }
    private void getDescription(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
    	System.out.println("get descritpion");
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
//    	String userid=request.getAttribute("sub").toString();
    	JsonObject object= new JsonObject();
    	StringBuilder jsonBuffer= new StringBuilder();
    	BufferedReader jsonreader= request.getReader();
    	String line;
    	while((line=jsonreader.readLine())!=null) {
    		
    		jsonBuffer.append(line);
    	}
    	JsonParser parser=new JsonParser();
    	object=parser.parse(jsonBuffer.toString()).getAsJsonObject();
    	int id=Integer.parseInt(object.get("bookId").getAsString());
    	System.out.println(id);
    	object.getAsJsonObject().remove("bookId");
    	
    	Book book=bookDao.getBookById(id);
//    	System.out.println(book);
    	CategoryDao c_d= new CategoryDao();
    	object.addProperty("title", book.getTitle());
    	object.addProperty("category",c_d.getCategoryById(book.getCategoryId()).getCategoryName());
    	object.addProperty("author", book.getAuthor());
    	object.addProperty("isbn", book.getIsbn());
    	object.addProperty("price", book.getPrice());
    	object.addProperty("publisher", book.getPublisher());
    	object.addProperty("year", book.getPublicationYear());
    	response.getWriter().write(object.toString());
    	
    }
    private void getAllImages(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
//    	String subDirectory = (String) request.getAttribute("sub");
  	  String DIRECTORY_PATH = "D:/Webprograming/CDAC project/images/" ;
        File directory = new File(DIRECTORY_PATH);
        List<List> imageDataList = new ArrayList<>();
        
        if (directory.exists() && directory.isDirectory()) {
        	for(File userid:directory.listFiles()	) {
//        		System.out.println(userid.getName());
            for (File subDir : userid.listFiles()) {
            	List<String> templist=new ArrayList<String>();
            	templist.add(userid.getName());
            	
          	  templist.add(subDir.getName());
          	  File front= new File(subDir+"/front");
                if (subDir.isDirectory()) {
                    for (File file : front.listFiles()) {
//              	  File directory = new File()
                        if (file.isFile()) {
                            try (FileInputStream fis = new FileInputStream(file)) {
                                byte[] bytes = new byte[(int) file.length()];
                                fis.read(bytes);
                                String base64Image = Base64.getEncoder().encodeToString(bytes);
                                String imageData = "data:image/jpeg;base64," + base64Image;
                                templist.add(imageData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                File back= new File(subDir+"/back");
                if (subDir.isDirectory()) {
                    for (File file : back.listFiles()) {
//              	  File directory = new File()
                        if (file.isFile()) {
                            try (FileInputStream fis = new FileInputStream(file)) {
                                byte[] bytes = new byte[(int) file.length()];
                                fis.read(bytes);
                                String base64Image = Base64.getEncoder().encodeToString(bytes);
                                String imageData = "data:image/jpeg;base64," + base64Image;
                                templist.add(imageData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                imageDataList.add(templist);
            }
        }
        }

        // Convert list to JSON
        Gson gson = new Gson();
        String json = gson.toJson(imageDataList);

        // Send JSON response
        response.setContentType("application/json");
        response.getWriter().write(json);
    }
    private void getCategory(HttpServletRequest request,HttpServletResponse response) throws IllegalStateException, IOException, ServletException {
    	 response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        JsonObject jsonResponse = new JsonObject();
    		for(Category categor:category.getAllCategories()) {
    			jsonResponse.addProperty(Integer.toString(categor.getCategoryId()),categor.getCategoryName());
    		}
    		response.getWriter().print(jsonResponse.toString());
    	}
    private void updateBook(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	try {
            // Get all parts of the request
    		
   		 Map map= new HashMap<String ,String>();
            for (Part part : request.getParts()) {
                String name = part.getName();
              
                    // This part is a form field
                    String fieldValue = extractFormField(part);
                    map.put(name, fieldValue);
               
            }
            Book book=bookDao.getBookById(Integer.parseInt(map.get("id").toString()));
            book.setTitle(map.get("title").toString());
            book.setAuthor(map.get("author").toString());
            book.setPublisher(map.get("publisher").toString());
            book.setPublicationYear(map.get("year").toString());
            book.setIsbn(map.get("isbn").toString());
            book.setPrice(BigDecimal.valueOf(Integer.parseInt(map.get("price").toString())));
            book.setCategoryId(Integer.parseInt(map.get("category").toString()));
            bookDao.updateBook(book);
            
            // Handle other processing or send response
            response.getWriter().println("Update Request processed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }
    private void createBook(HttpServletRequest request,HttpServletResponse response) throws IllegalStateException, IOException, ServletException {
    	 try {
    		 String userid=request.getAttribute("sub").toString();
             // Get all parts of the request
    		 Part frontfile=null;
    		 Part backfile=null;
    		 Book book=new Book();
    		 Map map= new HashMap<String ,String>();
             for (Part part : request.getParts()) {
                 String name = part.getName();
                 if (part.getContentType() != null) {
                     // This part is a file upload
//                     String fileName = extractFileName(part);
                	 if(name.equals("frontimage") ){
                		 frontfile=part;
                	 }
                	 else {
                	 backfile=part;
                	 }
                     // Save the file to the server
                     // Example: Files.copy(part.getInputStream(), Paths.get(uploadPath + File.separator + fileName));
                 } else {
                     // This part is a form field
                     String fieldValue = extractFormField(part);
                     map.put(name, fieldValue);
                 }
             }
             System.out.println(map);
             User newUser = new UserDao().getUserById(userid);
             map.put("phone_number", newUser.getPhoneNumber().toString());
             book.setTitle(map.get("title").toString());
             book.setAuthor(map.get("author").toString());
             book.setPublisher(map.get("publisher").toString());
             book.setPublicationYear(map.get("year").toString());
             book.setIsbn(map.get("isbn").toString());
             book.setPrice(BigDecimal.valueOf(Integer.parseInt(map.get("price").toString())));
             book.setCategoryId(Integer.parseInt(map.get("category").toString()));
             book.setSellerId(request.getAttribute("sub").toString());
             int book_id=bookDao.addBook(book);
             book.setBookId(book_id);
   
             createImage(frontfile,"./images/"+userid+'/'+Integer.toString(book_id)+"/front");
             createImage(backfile,"./images/"+userid+'/'+Integer.toString(book_id)+"/back");
//             String filename=extractFileName(file);
             
             
             
             
             String imageUrl = "./images/" +userid+'/'+Integer.toString(book_id);
             book.setCoverImageUrl(imageUrl);
             bookDao.updateBook(book);
             // Handle other processing or send response
             System.out.println(map);
             String status = null;
             System.out.println(map);
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
                 map.put("buyer", request.getAttribute("sub").toString());
                 String body=map.toString();
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
                 System.out.println("alsdkflsdk"+map.toString());
                 try (Transport tt = session.getTransport()) {
                     // acqruiring a connection to remote server
                     tt.connect("bookcomproject@gmail.com", "whlh twwg gpym qcgo");
                     tt.sendMessage(message,
                                    message.getAllRecipients());
                     status = "E-Mail Sent Successfully";
                 }
             }
             catch (MessagingException e) {
                 status = e.toString();
             }
//             catch (UnsupportedEncodingException e) {
//                 status = e.toString();
//             }
             // return the status of email
//             response.getWriter().print(status);
             response.getWriter().println("Request processed successfully"+status);
         } catch (Exception e) {
             e.printStackTrace();
             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
         }
     }
    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private static String extractFormField(Part part) throws IOException {
        // Read the value of the form field from the input stream
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream()))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
  


    
    
    private void createImage(Part part, String filename) throws Exception {
        // Extract the original filename
        String originalFileName = extractFileName(part);

        // Create a unique filename to avoid collisions
        String uniqueFileName = generateUniqueFileName(originalFileName);

        // Define the upload directory path
        String basePath = "D:\\Webprograming\\CDAC project";
        String uploadPath = basePath + File.separator + filename; // Append filename to the base path
        File uploadDir = new File(uploadPath);
        
        // Create the upload directory if it does not exist
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the file with a unique name
        try (InputStream fileContent = part.getInputStream();
             FileOutputStream fos = new FileOutputStream(new File(uploadPath + File.separator + uniqueFileName))) {
            int read;
            final byte[] bytes = new byte[1024];
            while ((read = fileContent.read(bytes)) != -1) {
                fos.write(bytes, 0, read);
            }
        } catch (Exception e) {
            throw new Exception("Error while saving file: " + e.getMessage(), e);
        }
    }

    // Generate a unique filename to prevent collisions
    private String generateUniqueFileName(String originalFileName) {
        String fileNameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        return fileNameWithoutExt + "_" + uniqueSuffix + fileExtension;
    }


    
}
package com.controller.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        System.out.println(filePart);
        if (filePart == null) {
            response.getWriter().println("No file uploaded.");
            return;
        }

        String fileName = getFileName(filePart);
        String uploadPath = "./eclipse-workspace/images"; // Replace with your desired upload directory path

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
        	System.out.println("Not exist");
            uploadDir.mkdirs();
        }
        System.out.println(uploadDir);
        try (InputStream fileContent = filePart.getInputStream();
             FileOutputStream fos = new FileOutputStream(new File(uploadPath + File.separator + fileName))) {
        	System.out.println(uploadPath + File.separator + fileName);
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
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}

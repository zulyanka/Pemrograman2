// Zulyan Widyaka
// 231011403446


package com.app;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TraditionalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h2>Metode web.xml (Tradisional)</h2>");
        out.println("<p>Servlet ini diatur melalui file <b>WEB-INF/web.xml</b></p>");
        out.println("</body></html>");
    }
}
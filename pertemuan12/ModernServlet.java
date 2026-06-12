// Zulyan Widyaka
// 231011403446


package com.app;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/modern")
public class ModernServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h2>Metode Anotasi (Modern)</h2>");
        out.println("<p>Servlet ini diatur menggunakan <b>@WebServlet(\"/modern\")</b></p>");
        out.println("</body></html>");
    }
}
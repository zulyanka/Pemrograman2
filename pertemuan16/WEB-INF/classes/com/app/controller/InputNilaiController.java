package com.app.controller;

import com.app.model.*;
import com.app.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/input-nilai")
public class InputNilaiController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) { response.sendRedirect("login"); return; }
        
        // Mengirim master data ke JSP untuk dropdown
        request.setAttribute("mhsList", JsonDB.getMahasiswa());
        request.setAttribute("mkList", JsonDB.getMataKuliah());
        
        request.getRequestDispatcher("/input-nilai.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) { response.sendRedirect("login"); return; }

        String nim = request.getParameter("nim");
        String kodeMk = request.getParameter("kodeMk");
        String nilaiAngkaStr = request.getParameter("nilaiAngka");

        if (nim != null && kodeMk != null && nilaiAngkaStr != null) {
            try {
                double nilaiAngka = Double.parseDouble(nilaiAngkaStr);
                String id = UUID.randomUUID().toString(); 
                Nilai newNilai = new Nilai(id, nim, kodeMk, nilaiAngka);
                
                JsonDB.addNilai(newNilai); // Pastikan method ini ada di JsonDB.java
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Setelah sukses simpan, arahkan user ke halaman laporan
        response.sendRedirect("laporan-nilai");
    }
}
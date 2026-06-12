package com.app.controller;

import com.app.model.*;
import com.app.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/laporan-nilai")
public class LaporanNilaiController extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) { response.sendRedirect("login"); return; }
        
        request.setAttribute("nilaiList", JsonDB.getNilai());
        request.setAttribute("mhsList", JsonDB.getMahasiswa());
        request.setAttribute("mkList", JsonDB.getMataKuliah());

        if ("pdf".equals(request.getParameter("export"))) {
            PdfGenerator.generateRekap(response, JsonDB.getNilai(), JsonDB.getMahasiswa(), JsonDB.getMataKuliah());
            return;
        }

        request.getRequestDispatcher("/laporan-nilai.jsp").forward(request, response);
    }
}
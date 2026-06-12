package com.app.controller;

import com.app.model.Mahasiswa;
import com.app.util.JsonDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/mahasiswa")
public class MahasiswaServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("login"); return;
        }
        
        String action = request.getParameter("action");
        List<Mahasiswa> list = JsonDB.getMahasiswa();
        
        if ("delete".equals(action)) {
            String nim = request.getParameter("nim");
            list.removeIf(m -> m.getNim().equals(nim));
            JsonDB.saveMahasiswa(list);
            response.sendRedirect("mahasiswa");
            return;
        }

        request.setAttribute("mahasiswaList", list);
        request.getRequestDispatcher("/mahasiswa.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nim = request.getParameter("nim");
        String nama = request.getParameter("nama");
        String kelas = request.getParameter("kelas");
        String jurusan = request.getParameter("jurusan");

        List<Mahasiswa> list = JsonDB.getMahasiswa();
        // Cek update atau create (Hapus data lama jika update, lalu masukkan baru)
        list.removeIf(m -> m.getNim().equals(nim)); 
        list.add(new Mahasiswa(nim, nama, kelas, jurusan));
        
        JsonDB.saveMahasiswa(list);
        response.sendRedirect("mahasiswa");
    }
}
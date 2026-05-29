package com.app.controller;
import com.app.model.MataKuliah;
import com.app.util.JsonDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/matakuliah")
public class MataKuliahServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) { response.sendRedirect("login"); return; }
        
        String action = request.getParameter("action");
        List<MataKuliah> list = JsonDB.getMataKuliah();
        
        if ("delete".equals(action)) {
            String kode = request.getParameter("kode");
            list.removeIf(m -> m.getKode().equals(kode));
            JsonDB.saveMataKuliah(list);
            response.sendRedirect("matakuliah");
            return;
        }
        request.setAttribute("mkList", list);
        request.getRequestDispatcher("/matakuliah.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String kode = request.getParameter("kode");
        String nama = request.getParameter("nama");
        int sks = Integer.parseInt(request.getParameter("sks"));

        List<MataKuliah> list = JsonDB.getMataKuliah();
        list.removeIf(m -> m.getKode().equals(kode)); 
        list.add(new MataKuliah(kode, nama, sks));
        
        JsonDB.saveMataKuliah(list);
        response.sendRedirect("matakuliah");
    }
}
package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * LoginServlet — Alternatif dari validasi.jsp menggunakan Servlet murni.
 *
 * Cara pakai:
 *   1. Uncomment <servlet> di web.xml, atau biarkan @WebServlet bekerja otomatis
 *   2. Ubah action form di index.jsp dari "validasi.jsp" menjadi "/login"
 *
 * Servlet cocok digunakan jika logika bisnis ingin dipisahkan dari tampilan (MVC).
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String VALID_USERNAME = "ADMIN";
    private static final String VALID_PASSWORD = "ADMIN";
    private static final int    DURASI_1_HARI  = 60 * 60 * 24; // 86400 detik

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String inputUsername = request.getParameter("username");
        String inputPassword = request.getParameter("password");

        if (VALID_USERNAME.equals(inputUsername) && VALID_PASSWORD.equals(inputPassword)) {
            // ── LOGIN BERHASIL ──

            // 1. Buat/ambil session dan simpan username
            HttpSession session = request.getSession(true);
            session.setAttribute("userLogin", inputUsername);
            session.setMaxInactiveInterval(DURASI_1_HARI);

            // 2. Buat Cookie dan kirim ke browser
            Cookie loginCookie = new Cookie("userLogin", inputUsername);
            loginCookie.setMaxAge(DURASI_1_HARI);
            loginCookie.setPath("/");
            response.addCookie(loginCookie);

            // 3. Teruskan ke validasi.jsp untuk menampilkan hasil
            request.setAttribute("statusLogin",  "sukses");
            request.setAttribute("pesanStatus",  "Login Berhasil! Selamat datang, " + inputUsername + "!");
            request.setAttribute("userDisplay",  inputUsername);
            request.getRequestDispatcher("validasi.jsp").forward(request, response);

        } else {
            // ── LOGIN GAGAL ──
            request.setAttribute("statusLogin", "gagal");
            request.setAttribute("pesanStatus", "Login Gagal! Username atau password salah.");
            request.getRequestDispatcher("validasi.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Akses GET langsung ke /login → redirect ke halaman login
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}

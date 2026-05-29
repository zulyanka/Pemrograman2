// Nama : I Gede Yoga Setiawan
// Nim : 231011401028
// Kelas : 06TPLE016

package com.app;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet({"/index", "/"})
public class IndexServlet extends HttpServlet {
    
    public IndexServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Memastikan encoding UTF-8 agar karakter tidak rusak
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String userName = "";
        if (!session.isNew()) {
            try {
                if (session.getAttribute("userName") != null) {
                    userName = session.getAttribute("userName").toString();
                }
            } catch (Exception ex) {}
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang='id'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Informasi Nilai Mahasiswa</title>");
        
        // Import Font Poppins & Icon FontAwesome
        out.println("    <link href='https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap' rel='stylesheet'>");
        out.println("    <link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css'>");
        
        out.println("    <style>");
        // Definisi Warna Tema (CSS Variables)
        out.println("        :root {");
        out.println("            --main-color: #2FA084;");
        out.println("            --main-hover: #24856d;");
        out.println("            --bg-color: #f4f7f6;");
        out.println("            --text-dark: #2c3e50;");
        out.println("            --text-muted: #7f8c8d;");
        out.println("            --sidebar-width: 260px;");
        out.println("        }");
        
        // Reset CSS
        out.println("        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }");
        out.println("        body { background-color: var(--bg-color); color: var(--text-dark); display: flex; min-height: 100vh; }");
        
        // CSS Sidebar
        out.println("        .sidebar { width: var(--sidebar-width); background: #ffffff; box-shadow: 2px 0 15px rgba(0,0,0,0.04); display: flex; flex-direction: column; position: fixed; height: 100%; z-index: 100; }");
        out.println("        .sidebar-header { background: var(--main-color); padding: 25px 20px; color: white; text-align: center; }");
        out.println("        .sidebar-header i { font-size: 32px; margin-bottom: 10px; }");
        out.println("        .sidebar-header h2 { font-size: 16px; font-weight: 600; letter-spacing: 0.5px; }");
        out.println("        .sidebar-menu { padding: 20px 0; overflow-y: auto; flex: 1; }");
        out.println("        .menu-title { padding: 15px 25px 5px; font-size: 11px; color: var(--text-muted); text-transform: uppercase; font-weight: 700; letter-spacing: 1px; }");
        out.println("        .sidebar-menu a { display: flex; align-items: center; padding: 12px 25px; color: var(--text-dark); text-decoration: none; font-size: 14px; font-weight: 500; transition: all 0.3s ease; border-left: 4px solid transparent; }");
        out.println("        .sidebar-menu a i { width: 25px; font-size: 16px; color: var(--text-muted); transition: 0.3s; }");
        out.println("        .sidebar-menu a:hover, .sidebar-menu a.active { background: #f0f9f7; color: var(--main-color); border-left-color: var(--main-color); }");
        out.println("        .sidebar-menu a:hover i, .sidebar-menu a.active i { color: var(--main-color); }");
        
        // CSS Main Content
        out.println("        .main-content { margin-left: var(--sidebar-width); flex: 1; display: flex; flex-direction: column; }");
        out.println("        .topbar { background: #ffffff; padding: 20px 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.03); display: flex; justify-content: space-between; align-items: center; }");
        out.println("        .topbar .title-kampus { font-size: 18px; font-weight: 700; color: var(--main-color); }");
        out.println("        .topbar .subtitle { font-size: 12px; color: var(--text-muted); }");
        
        // CSS Area Konten
        out.println("        .content-area { padding: 40px; flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; text-align: center; }");
        out.println("        .welcome-text { font-size: 32px; font-weight: 700; color: var(--text-dark); margin-bottom: 30px; }");
        out.println("        .welcome-text span { color: var(--main-color); }");
        
        // CSS Card Identitas
        out.println("        .id-card { background: #ffffff; border-radius: 12px; box-shadow: 0 8px 25px rgba(0,0,0,0.05); overflow: hidden; width: 100%; max-width: 450px; position: relative; border-top: 5px solid var(--main-color); text-align: left; }");
        out.println("        .id-card-header { padding: 20px 25px; border-bottom: 1px solid #eee; display: flex; align-items: center; gap: 12px; }");
        out.println("        .id-card-header i { font-size: 24px; color: var(--main-color); }");
        out.println("        .id-card-header h3 { font-size: 18px; font-weight: 600; color: var(--text-dark); }");
        out.println("        .id-card-body { padding: 25px; }");
        out.println("        .info-group { display: flex; align-items: center; margin-bottom: 15px; }");
        out.println("        .info-group:last-child { margin-bottom: 0; }");
        out.println("        .info-icon { width: 40px; height: 40px; border-radius: 8px; background: #e8f5f2; display: flex; align-items: center; justify-content: center; margin-right: 15px; color: var(--main-color); }");
        out.println("        .info-text p { font-size: 12px; color: var(--text-muted); margin-bottom: 2px; text-transform: uppercase; letter-spacing: 0.5px; }");
        out.println("        .info-text h4 { font-size: 15px; font-weight: 600; color: var(--text-dark); }");
        
        // CSS Footer
        out.println("        .footer { background: #ffffff; padding: 20px; text-align: center; font-size: 13px; color: var(--text-muted); border-top: 1px solid #eee; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        
        // ================= SIDEBAR KIRI =================
        out.println("    <aside class='sidebar'>");
        out.println("        <div class='sidebar-header'>");
        out.println("            <i class='fas fa-graduation-cap'></i>");
        out.println("            <h2>SISTEM NILAI</h2>");
        out.println("        </div>");
        out.println("        <div class='sidebar-menu'>");
        out.println("            <a href='#' class='active'><i class='fas fa-home'></i> Beranda</a>");
        
        out.println("            <div class='menu-title'>Master Data</div>");
        out.println("            <a href='#'><i class='fas fa-users'></i> Data Mahasiswa</a>");
        out.println("            <a href='#'><i class='fas fa-book-open'></i> Mata Kuliah</a>");
        
        out.println("            <div class='menu-title'>Transaksi</div>");
        out.println("            <a href='#'><i class='fas fa-keyboard'></i> Input Nilai</a>");
        
        out.println("            <div class='menu-title'>Laporan</div>");
        out.println("            <a href='#'><i class='fas fa-file-alt'></i> Rekap Nilai</a>");
        
        out.println("            <div class='menu-title'>Sistem</div>");
        out.println("            <a href='LoginController'><i class='fas fa-sign-in-alt'></i> Login</a>");
        out.println("        </div>");
        out.println("    </aside>");

        // ================= KONTEN KANAN =================
        out.println("    <main class='main-content'>");
        
        // TOPBAR
        out.println("        <header class='topbar'>");
        out.println("            <div>");
        out.println("                <div class='title-kampus'>Universitas Pamulang</div>");
        out.println("                <div class='subtitle'>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan</div>");
        out.println("            </div>");
        out.println("            <div>");
        out.println("                <i class='fas fa-bell' style='color: var(--text-muted); margin-right: 15px; cursor: pointer;'></i>");
        out.println("                <i class='fas fa-user-circle' style='font-size: 24px; color: var(--main-color); cursor: pointer;'></i>");
        out.println("            </div>");
        out.println("        </header>");

        // AREA TENGAH
        out.println("        <section class='content-area'>");
        out.println("            <h1 class='welcome-text'>Selamat Datang di <span>Sistem Informasi</span></h1>");
        
        if (!userName.isEmpty()) {
            out.println("            <h2 style='margin-bottom: 20px; color: var(--text-muted);'>Halo, " + userName + "!</h2>");
        }
        
        // KARTU IDENTITAS MAHASISWA
        out.println("            <div class='id-card'>");
        out.println("                <div class='id-card-header'>");
        out.println("                    <i class='fas fa-id-badge'></i>");
        out.println("                    <h3>Profil Mahasiswa</h3>");
        out.println("                </div>");
        out.println("                <div class='id-card-body'>");
        
        out.println("                    <div class='info-group'>");
        out.println("                        <div class='info-icon'><i class='fas fa-user'></i></div>");
        out.println("                        <div class='info-text'>");
        out.println("                            <p>Nama Lengkap</p>");
        out.println("                            <h4>I Gede Yoga Setiawan</h4>");
        out.println("                        </div>");
        out.println("                    </div>");
        
        out.println("                    <div class='info-group'>");
        out.println("                        <div class='info-icon'><i class='fas fa-hashtag'></i></div>");
        out.println("                        <div class='info-text'>");
        out.println("                            <p>Nomor Induk Mahasiswa (NIM)</p>");
        out.println("                            <h4>231011401028</h4>");
        out.println("                        </div>");
        out.println("                    </div>");
        
        out.println("                    <div class='info-group'>");
        out.println("                        <div class='info-icon'><i class='fas fa-door-open'></i></div>");
        out.println("                        <div class='info-text'>");
        out.println("                            <p>Kelas</p>");
        out.println("                            <h4>06TPLE016</h4>");
        out.println("                        </div>");
        out.println("                    </div>");
        
        out.println("                </div>");
        out.println("            </div>");
        out.println("        </section>");

        // FOOTER
        out.println("        <footer class='footer'>");
        out.println("            Copyright &copy; 2026 Universitas Pamulang | Aplikasi Administrasi Nilai Web");
        out.println("        </footer>");
        
        out.println("    </main>");
        out.println("</body>");
        out.println("</html>");
    }
}
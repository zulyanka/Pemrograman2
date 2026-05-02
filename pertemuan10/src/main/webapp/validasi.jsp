<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // ============================================================
    //  PROSES VALIDASI LOGIN
    // ============================================================

    final String VALID_USERNAME = "ADMIN";
    final String VALID_PASSWORD = "ADMIN";
    final int    DURASI_1_HARI  = 60 * 60 * 24; // 86400 detik

    String statusLogin  = "";   // "sukses" | "gagal" | "refresh"
    String pesanStatus  = "";
    String userDisplay  = "";

    // Ambil data form (POST) — bisa null jika halaman di-refresh
    String inputUsername = request.getParameter("username");
    String inputPassword = request.getParameter("password");

    // ── KASUS 1: Form dikirim (POST request) ──────────────────
    if (inputUsername != null && inputPassword != null) {

        if (inputUsername.equals(VALID_USERNAME) && inputPassword.equals(VALID_PASSWORD)) {
            // ── LOGIN BERHASIL ──
            statusLogin = "sukses";
            userDisplay = inputUsername;
            pesanStatus = "Login Berhasil! Selamat datang, " + inputUsername + "!";

            // 1. Simpan ke SESSION
            session.setAttribute("userLogin", inputUsername);
            session.setMaxInactiveInterval(DURASI_1_HARI);  // session berlaku 1 hari

            // 2. Buat dan simpan COOKIE
            Cookie loginCookie = new Cookie("userLogin", inputUsername);
            loginCookie.setMaxAge(DURASI_1_HARI);   // cookie berlaku 1 hari
            loginCookie.setPath("/");               // berlaku di seluruh aplikasi
            response.addCookie(loginCookie);

        } else {
            // ── LOGIN GAGAL ──
            statusLogin = "gagal";
            pesanStatus = "Login Gagal! Username atau password salah.";
        }

    // ── KASUS 2: Refresh / akses langsung (GET request) ──────
    } else {
        String sessionUser = (String) session.getAttribute("userLogin");

        if (sessionUser != null) {
            // Session masih aktif
            statusLogin = "refresh";
            userDisplay = sessionUser;
            pesanStatus = "Halaman di-refresh. Data Session & Cookie masih terbaca!";
        } else {
            // Tidak ada session, redirect ke login
            response.sendRedirect("index.jsp");
            return;
        }
    }

    // ── Baca semua Cookie yang ada ────────────────────────────
    Cookie[] allCookies  = request.getCookies();
    String   cookieValue = "Tidak ada cookie";

    if (allCookies != null) {
        for (Cookie c : allCookies) {
            if ("userLogin".equals(c.getName())) {
                cookieValue = c.getValue();
                break;
            }
        }
    }

    // ── Baca Session ──────────────────────────────────────────
    String sessionValue   = (String) session.getAttribute("userLogin");
    String sessionId      = session.getId();
    long   sessionCreated = session.getCreationTime();
    int    sessionTimeout = session.getMaxInactiveInterval();
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hasil Validasi - JSP Session & Cookie</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #1a1a2e, #16213e, #0f3460);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }

        .container {
            width: 100%;
            max-width: 560px;
        }

        /* ── Status Card ── */
        .status-card {
            border-radius: 16px;
            padding: 28px 32px;
            text-align: center;
            margin-bottom: 20px;
            border: 1px solid transparent;
        }

        .status-card.sukses {
            background: rgba(34, 197, 94, 0.1);
            border-color: rgba(34, 197, 94, 0.3);
        }

        .status-card.gagal {
            background: rgba(233, 69, 96, 0.1);
            border-color: rgba(233, 69, 96, 0.3);
        }

        .status-card.refresh {
            background: rgba(99, 202, 183, 0.1);
            border-color: rgba(99, 202, 183, 0.3);
        }

        .status-card .icon {
            font-size: 52px;
            display: block;
            margin-bottom: 12px;
        }

        .status-card h2 {
            font-size: 20px;
            font-weight: 700;
            margin-bottom: 6px;
        }

        .status-card.sukses  h2 { color: #22c55e; }
        .status-card.gagal   h2 { color: #e94560; }
        .status-card.refresh h2 { color: #63cab7; }

        .status-card p {
            color: rgba(255, 255, 255, 0.65);
            font-size: 14px;
            line-height: 1.5;
        }

        /* ── Info Panel ── */
        .info-panel {
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 12px;
            padding: 20px 24px;
            margin-bottom: 16px;
        }

        .info-panel h3 {
            color: rgba(255, 255, 255, 0.9);
            font-size: 13px;
            font-weight: 600;
            letter-spacing: 1px;
            text-transform: uppercase;
            margin-bottom: 14px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            padding: 8px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.06);
            gap: 12px;
        }

        .info-row:last-child {
            border-bottom: none;
            padding-bottom: 0;
        }

        .info-row .label {
            color: rgba(255, 255, 255, 0.4);
            font-size: 12px;
            flex-shrink: 0;
        }

        .info-row .value {
            color: rgba(255, 255, 255, 0.85);
            font-size: 13px;
            font-family: 'Courier New', monospace;
            word-break: break-all;
            text-align: right;
        }

        .badge {
            display: inline-block;
            padding: 2px 10px;
            border-radius: 999px;
            font-size: 11px;
            font-weight: 600;
        }

        .badge.green  { background: rgba(34,  197, 94,  0.2); color: #22c55e; }
        .badge.blue   { background: rgba(96,  165, 250, 0.2); color: #60a5fa; }
        .badge.yellow { background: rgba(251, 191, 36,  0.2); color: #fbbf24; }

        /* ── Action Buttons ── */
        .btn-group {
            display: flex;
            gap: 12px;
            margin-top: 4px;
        }

        .btn {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
            transition: opacity 0.2s, transform 0.1s;
            display: block;
        }

        .btn:active { transform: scale(0.98); }

        .btn-back {
            background: rgba(255, 255, 255, 0.08);
            color: rgba(255, 255, 255, 0.7);
            border: 1px solid rgba(255, 255, 255, 0.12);
        }

        .btn-back:hover { background: rgba(255, 255, 255, 0.12); }

        .btn-logout {
            background: linear-gradient(135deg, #e94560, #c73652);
            color: #ffffff;
        }

        .btn-logout:hover { opacity: 0.85; }
    </style>
</head>
<body>
<div class="container">

    <!-- ── STATUS CARD ── -->
    <div class="status-card <%= statusLogin %>">
        <% if ("sukses".equals(statusLogin)) { %>
            <span class="icon">✅</span>
            <h2>Login Berhasil!</h2>
        <% } else if ("gagal".equals(statusLogin)) { %>
            <span class="icon">❌</span>
            <h2>Login Gagal!</h2>
        <% } else { %>
            <span class="icon">🔄</span>
            <h2>Session Masih Aktif</h2>
        <% } %>
        <p><%= pesanStatus %></p>
    </div>

    <!-- ── Hanya tampilkan detail jika login berhasil atau refresh ── -->
    <% if (!"gagal".equals(statusLogin)) { %>

        <!-- Info SESSION -->
        <div class="info-panel">
            <h3>📦 &nbsp;Data Session</h3>

            <div class="info-row">
                <span class="label">Status</span>
                <span class="value"><span class="badge green">AKTIF</span></span>
            </div>
            <div class="info-row">
                <span class="label">Username (Attribute)</span>
                <span class="value"><%= sessionValue != null ? sessionValue : "-" %></span>
            </div>
            <div class="info-row">
                <span class="label">Session ID</span>
                <span class="value"><%= sessionId.substring(0, Math.min(24, sessionId.length())) %>...</span>
            </div>
            <div class="info-row">
                <span class="label">Dibuat Pada</span>
                <span class="value"><%= new java.util.Date(sessionCreated) %></span>
            </div>
            <div class="info-row">
                <span class="label">Masa Aktif</span>
                <span class="value">
                    <span class="badge blue"><%= sessionTimeout / 3600 %> jam (<%= sessionTimeout %> detik)</span>
                </span>
            </div>
        </div>

        <!-- Info COOKIE -->
        <div class="info-panel">
            <h3>🍪 &nbsp;Data Cookie</h3>

            <div class="info-row">
                <span class="label">Status</span>
                <span class="value">
                    <span class="badge <%= !cookieValue.equals("Tidak ada cookie") ? "green" : "yellow" %>">
                        <%= !cookieValue.equals("Tidak ada cookie") ? "TERSIMPAN" : "TIDAK ADA" %>
                    </span>
                </span>
            </div>
            <div class="info-row">
                <span class="label">Nama Cookie</span>
                <span class="value">userLogin</span>
            </div>
            <div class="info-row">
                <span class="label">Nilai Cookie</span>
                <span class="value"><%= cookieValue %></span>
            </div>
            <div class="info-row">
                <span class="label">Masa Aktif</span>
                <span class="value">
                    <span class="badge blue">1 hari (86400 detik)</span>
                </span>
            </div>
            <div class="info-row">
                <span class="label">Scope Path</span>
                <span class="value">/</span>
            </div>
        </div>

    <% } %>

    <!-- ── ACTION BUTTONS ── -->
    <div class="btn-group">
        <% if ("gagal".equals(statusLogin)) { %>
            <!-- Jika gagal, hanya ada tombol kembali -->
            <a href="index.jsp" class="btn btn-back" style="flex: 2;">
                ← Kembali ke Halaman Login
            </a>
        <% } else { %>
            <a href="index.jsp" class="btn btn-back">
                ← Kembali
            </a>
            <a href="logout.jsp" class="btn btn-logout">
                🚪 &nbsp;Logout
            </a>
        <% } %>
    </div>

</div>
</body>
</html>

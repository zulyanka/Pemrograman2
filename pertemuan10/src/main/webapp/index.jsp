<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Jika sudah login (session masih aktif), langsung redirect ke validasi
    String userSession = (String) session.getAttribute("userLogin");
    if (userSession != null) {
        response.sendRedirect("validasi.jsp");
        return;
    }

    // Cek apakah ada cookie username
    String userCookie = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("userLogin".equals(c.getName())) {
                userCookie = c.getValue();
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Aplikasi JSP Session & Cookie</title>
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
        }

        .login-card {
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 16px;
            padding: 40px;
            width: 100%;
            max-width: 420px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
        }

        .login-header {
            text-align: center;
            margin-bottom: 32px;
        }

        .login-header .icon {
            font-size: 48px;
            margin-bottom: 12px;
            display: block;
        }

        .login-header h1 {
            color: #ffffff;
            font-size: 22px;
            font-weight: 600;
        }

        .login-header p {
            color: rgba(255, 255, 255, 0.5);
            font-size: 13px;
            margin-top: 6px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            color: rgba(255, 255, 255, 0.7);
            font-size: 13px;
            font-weight: 500;
            margin-bottom: 8px;
            letter-spacing: 0.5px;
        }

        .form-group input {
            width: 100%;
            padding: 12px 16px;
            background: rgba(255, 255, 255, 0.08);
            border: 1px solid rgba(255, 255, 255, 0.15);
            border-radius: 8px;
            color: #ffffff;
            font-size: 14px;
            outline: none;
            transition: border-color 0.3s, background 0.3s;
        }

        .form-group input:focus {
            border-color: #e94560;
            background: rgba(233, 69, 96, 0.05);
        }

        .form-group input::placeholder {
            color: rgba(255, 255, 255, 0.3);
        }

        .btn-login {
            width: 100%;
            padding: 13px;
            background: linear-gradient(135deg, #e94560, #c73652);
            border: none;
            border-radius: 8px;
            color: white;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition: opacity 0.3s, transform 0.1s;
            margin-top: 8px;
            letter-spacing: 0.5px;
        }

        .btn-login:hover {
            opacity: 0.9;
        }

        .btn-login:active {
            transform: scale(0.99);
        }

        .cookie-info {
            margin-top: 24px;
            padding: 12px 16px;
            background: rgba(99, 202, 183, 0.1);
            border: 1px solid rgba(99, 202, 183, 0.3);
            border-radius: 8px;
        }

        .cookie-info p {
            color: #63cab7;
            font-size: 12px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .hint {
            margin-top: 20px;
            text-align: center;
            padding: 10px;
            background: rgba(255, 255, 255, 0.04);
            border-radius: 8px;
        }

        .hint p {
            color: rgba(255, 255, 255, 0.35);
            font-size: 12px;
        }

        .hint code {
            color: rgba(233, 69, 96, 0.8);
            font-family: monospace;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="login-card">

        <div class="login-header">
            <span class="icon">🔐</span>
            <h1>Login Portal</h1>
            <p>Pemrograman 2 — Session &amp; Cookies</p>
        </div>

        <!-- Form Login -->
        <form action="validasi.jsp" method="post">
            <div class="form-group">
                <label for="username">USERNAME</label>
                <input
                    type="text"
                    id="username"
                    name="username"
                    placeholder="Masukkan username..."
                    value="<%= userCookie != null ? userCookie : "" %>"
                    autocomplete="off"
                    required
                />
            </div>

            <div class="form-group">
                <label for="password">PASSWORD</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Masukkan password..."
                    required
                />
            </div>

            <button type="submit" class="btn-login">🚀 &nbsp;MASUK</button>
        </form>

        <!-- Info Cookie Tersimpan -->
        <% if (userCookie != null) { %>
        <div class="cookie-info">
            <p>🍪 Cookie ditemukan: Username <strong>&nbsp;<%= userCookie %>&nbsp;</strong> sudah tersimpan</p>
        </div>
        <% } %>

        <!-- Petunjuk untuk mahasiswa -->
        <div class="hint">
            <p>Gunakan <code>ADMIN</code> / <code>ADMIN</code> untuk login</p>
        </div>

    </div>
</body>
</html>

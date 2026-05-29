<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang='id'>
<head>
    <meta charset='UTF-8'>
    <title>Sistem Nilai - Universitas Pamulang</title>
    <link href='https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap' rel='stylesheet'>
    <link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css'>
    <style>
        /* Masukkan seluruh blok CSS dari file IndexServlet.java lama Anda ke sini */
        :root { --main-color: #2FA084; --bg-color: #f4f7f6; --text-dark: #2c3e50; --sidebar-width: 260px; }
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body { background-color: var(--bg-color); display: flex; min-height: 100vh; }
        /* ... (Copy sisa CSS sidebar, topbar, dll dari kode asli Anda) ... */
        table { width: 100%; border-collapse: collapse; margin-top: 20px; background: #fff; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        th, td { padding: 12px 15px; border-bottom: 1px solid #eee; text-align: left; }
        th { background: var(--main-color); color: white; }
        .btn { padding: 8px 12px; border: none; border-radius: 4px; color: white; cursor: pointer; text-decoration: none; }
        .btn-primary { background: var(--main-color); }
        .btn-danger { background: #e74c3c; }
    </style>
</head>
<body>
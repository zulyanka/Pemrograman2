<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang='id'>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Sistem Nilai - Universitas Pamulang</title>
    
    <link href='https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap' rel='stylesheet'>
    <link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css'>
    
    <style>
        /* ================= VARIABEL WARNA ================= */
        :root {
            --main-color: #2FA084;
            --main-hover: #24856d;
            --bg-color: #f4f7f6;
            --text-dark: #2c3e50;
            --text-muted: #7f8c8d;
            --sidebar-width: 260px;
        }
        
        /* ================= RESET CSS ================= */
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body { background-color: var(--bg-color); color: var(--text-dark); display: flex; min-height: 100vh; overflow-x: hidden; }
        
        /* ================= SIDEBAR ================= */
        .sidebar { width: var(--sidebar-width); background: #ffffff; box-shadow: 2px 0 15px rgba(0,0,0,0.04); display: flex; flex-direction: column; position: fixed; height: 100%; z-index: 100; }
        .sidebar-header { background: var(--main-color); padding: 25px 20px; color: white; text-align: center; }
        .sidebar-header i { font-size: 32px; margin-bottom: 10px; }
        .sidebar-header h2 { font-size: 16px; font-weight: 600; letter-spacing: 0.5px; }
        .sidebar-menu { padding: 20px 0; overflow-y: auto; flex: 1; }
        .menu-title { padding: 15px 25px 5px; font-size: 11px; color: var(--text-muted); text-transform: uppercase; font-weight: 700; letter-spacing: 1px; }
        .sidebar-menu a { display: flex; align-items: center; padding: 12px 25px; color: var(--text-dark); text-decoration: none; font-size: 14px; font-weight: 500; transition: all 0.3s ease; border-left: 4px solid transparent; }
        .sidebar-menu a i { width: 25px; font-size: 16px; color: var(--text-muted); transition: 0.3s; }
        .sidebar-menu a:hover, .sidebar-menu a.active { background: #f0f9f7; color: var(--main-color); border-left-color: var(--main-color); }
        .sidebar-menu a:hover i, .sidebar-menu a.active i { color: var(--main-color); }
        
        /* ================= MAIN CONTENT & TOPBAR ================= */
        .main-content { margin-left: var(--sidebar-width); flex: 1; display: flex; flex-direction: column; min-height: 100vh; width: calc(100% - var(--sidebar-width)); }
        .topbar { background: #ffffff; padding: 20px 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.03); display: flex; justify-content: space-between; align-items: center; width: 100%; }
        .topbar .title-kampus { font-size: 18px; font-weight: 700; color: var(--main-color); }
        .topbar .subtitle { font-size: 12px; color: var(--text-muted); }
        
        /* ================= AREA KONTEN (DASHBOARD) ================= */
        .content-area { padding: 40px; flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; text-align: center; }
        .welcome-text { font-size: 32px; font-weight: 700; color: var(--text-dark); margin-bottom: 30px; }
        .welcome-text span { color: var(--main-color); }
        
        /* ================= ID CARD ================= */
        .id-card { background: #ffffff; border-radius: 12px; box-shadow: 0 8px 25px rgba(0,0,0,0.05); overflow: hidden; width: 100%; max-width: 450px; position: relative; border-top: 5px solid var(--main-color); text-align: left; }
        .id-card-header { padding: 20px 25px; border-bottom: 1px solid #eee; display: flex; align-items: center; gap: 12px; }
        .id-card-header i { font-size: 24px; color: var(--main-color); }
        .id-card-header h3 { font-size: 18px; font-weight: 600; color: var(--text-dark); }
        .id-card-body { padding: 25px; }
        .info-group { display: flex; align-items: center; margin-bottom: 15px; }
        .info-group:last-child { margin-bottom: 0; }
        .info-icon { width: 40px; height: 40px; border-radius: 8px; background: #e8f5f2; display: flex; align-items: center; justify-content: center; margin-right: 15px; color: var(--main-color); }
        .info-text p { font-size: 12px; color: var(--text-muted); margin-bottom: 2px; text-transform: uppercase; letter-spacing: 0.5px; }
        .info-text h4 { font-size: 15px; font-weight: 600; color: var(--text-dark); }
        
        /* ================= CSS UNTUK CRUD & TABEL ================= */
        .page-title { font-size: 22px; color: var(--text-dark); margin-bottom: 20px; font-weight: 600; border-bottom: 2px solid var(--main-color); padding-bottom: 10px; display: inline-block;}
        .form-container { background: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.03); margin-bottom: 25px; }
        .form-container input, .form-container select { padding: 10px 12px; margin-right: 10px; margin-bottom: 10px; border: 1px solid #ddd; border-radius: 5px; font-size: 14px; outline: none; transition: 0.3s; }
        .form-container input:focus, .form-container select:focus { border-color: var(--main-color); }
        
        table { width: 100%; border-collapse: collapse; background: #fff; box-shadow: 0 2px 10px rgba(0,0,0,0.03); border-radius: 8px; overflow: hidden; }
        th, td { padding: 14px 20px; border-bottom: 1px solid #f0f0f0; text-align: left; font-size: 14px; }
        th { background: var(--main-color); color: white; font-weight: 500; letter-spacing: 0.5px; }
        tr:hover { background: #f9fbfb; }
        
        /* ================= TOMBOL ================= */
        .btn { padding: 10px 18px; border: none; border-radius: 5px; color: white; cursor: pointer; text-decoration: none; font-size: 14px; font-weight: 500; display: inline-block; transition: 0.3s; }
        .btn-primary { background: var(--main-color); }
        .btn-primary:hover { background: var(--main-hover); }
        .btn-danger { background: #e74c3c; }
        .btn-danger:hover { background: #c0392b; }
        .btn-sm { padding: 6px 12px; font-size: 12px; }
        
        .footer { background: #ffffff; padding: 20px; text-align: center; font-size: 13px; color: var(--text-muted); border-top: 1px solid #eee; margin-top: auto;}
    </style>
</head>
<body>
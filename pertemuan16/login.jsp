<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sistem Nilai Universitas Pamulang</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body { background-color: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; }
        
        .login-container { background: white; padding: 40px; border-radius: 12px; box-shadow: 0 8px 25px rgba(0,0,0,0.05); width: 100%; max-width: 400px; text-align: center; border-top: 5px solid #2FA084; }
        .login-header i { font-size: 45px; color: #2FA084; margin-bottom: 10px; }
        .login-header h2 { color: #2c3e50; font-size: 24px; margin-bottom: 5px; font-weight: 600; }
        .login-header p { color: #7f8c8d; font-size: 14px; margin-bottom: 30px; }
        
        .input-group { margin-bottom: 20px; text-align: left; }
        .input-group label { display: block; margin-bottom: 8px; color: #2c3e50; font-weight: 500; font-size: 13px; text-transform: uppercase; letter-spacing: 0.5px; }
        .input-group input { width: 100%; padding: 12px 15px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; outline: none; transition: 0.3s; background: #fafafa; }
        .input-group input:focus { border-color: #2FA084; box-shadow: 0 0 0 3px rgba(47, 160, 132, 0.15); background: white; }
        
        .btn-login { width: 100%; padding: 12px; background: #2FA084; color: white; border: none; border-radius: 8px; font-size: 15px; font-weight: 600; cursor: pointer; transition: 0.3s; margin-top: 10px; }
        .btn-login:hover { background: #24856d; transform: translateY(-1px); }
        
        .error-msg { color: #e74c3c; font-size: 13px; margin-bottom: 20px; display: block; background: #fdf2f2; padding: 12px; border-radius: 8px; border-left: 4px solid #e74c3c; text-align: left;}
    </style>
</head>
<body>

    <div class="login-container">
        <div class="login-header">
            <i class="fas fa-graduation-cap"></i>
            <h2>Sistem Nilai</h2>
            <p>Universitas Pamulang</p>
        </div>
        
        <% if ("1".equals(request.getParameter("error"))) { %>
            <div class="error-msg">
                <i class="fas fa-exclamation-circle" style="margin-right: 5px;"></i> Username atau Password salah!
            </div>
        <% } %>

        <form action="login" method="POST">
            <div class="input-group">
                <label>Username</label>
                <input type="text" name="username" placeholder="Masukkan username..." required autocomplete="off">
            </div>
            <div class="input-group">
                <label>Password</label>
                <input type="password" name="password" placeholder="Masukkan password..." required>
            </div>
            <button type="submit" class="btn-login">Masuk <i class="fas fa-arrow-right" style="margin-left: 5px;"></i></button>
        </form>
    </div>

</body>
</html>
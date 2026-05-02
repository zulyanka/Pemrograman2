<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // ============================================================
    //  LOGOUT — Hapus Session & Cookie
    // ============================================================

    // 1. Hapus semua attribute dalam session
    session.removeAttribute("userLogin");

    // 2. Invalidate (hancurkan) seluruh session
    session.invalidate();

    // 3. Hapus cookie dengan men-set MaxAge = 0
    Cookie logoutCookie = new Cookie("userLogin", "");
    logoutCookie.setMaxAge(0);   // 0 = hapus cookie segera
    logoutCookie.setPath("/");
    response.addCookie(logoutCookie);

    // 4. Redirect ke halaman login
    response.sendRedirect("index.jsp");
%>

<%@ page import="com.app.model.*" %>
<%@ page import="java.util.List" %>
<jsp:include page="layout/header.jsp" />
<jsp:include page="layout/sidebar.jsp" />

<main class='main-content'>
    <div class="topbar">
        <div class="title-kampus">Sistem Administrasi Nilai</div>
        <div class="subtitle">Universitas Pamulang</div>
    </div>
    
    <section style="padding: 40px 30px;">
        <h2 class="page-title" style="border-bottom: none; margin-bottom: 30px;"><i class="fas fa-edit"></i> Input Nilai Mahasiswa</h2>
        
        <div class="form-container" style="max-width: 600px; background: #fff; padding: 35px; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.05); border-top: 4px solid var(--main-color);">
            <form action="input-nilai" method="POST">
                
                <div style="margin-bottom: 20px;">
                    <label style="display: block; font-weight: 600; margin-bottom: 8px; font-size: 14px;">Pilih Mahasiswa</label>
                    <select name="nim" required style="width: 100%; padding: 12px 15px; border: 1px solid #e1e5eb; border-radius: 8px; outline: none; background: #f8fafc;">
                        <option value="">-- Silakan Pilih Mahasiswa --</option>
                        <% 
                        List<Mahasiswa> mList = (List<Mahasiswa>) request.getAttribute("mhsList");
                        if(mList != null) { for(Mahasiswa m : mList) { 
                        %>
                            <option value="<%= m.getNim() %>"><%= m.getNim() %> - <%= m.getNama() %></option>
                        <% } } %>
                    </select>
                </div>

                <div style="margin-bottom: 20px;">
                    <label style="display: block; font-weight: 600; margin-bottom: 8px; font-size: 14px;">Pilih Mata Kuliah</label>
                    <select name="kodeMk" required style="width: 100%; padding: 12px 15px; border: 1px solid #e1e5eb; border-radius: 8px; outline: none; background: #f8fafc;">
                        <option value="">-- Silakan Pilih Mata Kuliah --</option>
                        <% 
                        List<MataKuliah> mkList = (List<MataKuliah>) request.getAttribute("mkList");
                        if(mkList != null) { for(MataKuliah mk : mkList) { 
                        %>
                            <option value="<%= mk.getKode() %>"><%= mk.getKode() %> - <%= mk.getNama() %></option>
                        <% } } %>
                    </select>
                </div>

                <div style="margin-bottom: 30px;">
                    <label style="display: block; font-weight: 600; margin-bottom: 8px; font-size: 14px;">Nilai Angka</label>
                    <input type="number" name="nilaiAngka" step="0.01" min="0" max="100" required placeholder="0 - 100" style="width: 100%; padding: 12px 15px; border: 1px solid #e1e5eb; border-radius: 8px; outline: none; background: #f8fafc;">
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; padding: 14px; font-size: 15px; font-weight: 600; border-radius: 8px;">
                    <i class="fas fa-save"></i> Simpan Data Nilai
                </button>
            </form>
        </div>
    </section>
</main>
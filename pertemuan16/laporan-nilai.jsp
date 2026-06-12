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
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px;">
            <h2 class="page-title" style="margin-bottom: 0; border: none;"><i class="fas fa-file-alt"></i> Laporan Rekapitulasi Nilai</h2>
            <a href="laporan-nilai?export=pdf" class="btn btn-danger" style="border-radius: 8px; padding: 10px 20px;">
                <i class="fas fa-file-pdf"></i> Export ke PDF
            </a>
        </div>

        <div style="background: #fff; border-radius: 12px; box-shadow: 0 5px 20px rgba(0,0,0,0.03); overflow: hidden;">
            <table style="width: 100%; border-collapse: collapse;">
                <tr style="background: var(--main-color); color: white;">
                    <th style="padding: 16px 20px;">NIM</th>
                    <th style="padding: 16px 20px;">Nama Mahasiswa</th>
                    <th style="padding: 16px 20px;">Mata Kuliah</th>
                    <th style="padding: 16px 20px; text-align: center;">Nilai Angka</th>
                    <th style="padding: 16px 20px; text-align: center;">Grade</th>
                </tr>
                
                <% 
                List<Nilai> nList = (List<Nilai>) request.getAttribute("nilaiList");
                List<Mahasiswa> mList = (List<Mahasiswa>) request.getAttribute("mhsList");
                List<MataKuliah> mkList = (List<MataKuliah>) request.getAttribute("mkList");

                if (nList != null && !nList.isEmpty()) {
                    for (Nilai n : nList) {
                        String nama = "N/A";
                        String mk = "N/A";
                        if(mList != null) for(Mahasiswa m : mList) if(m.getNim().equals(n.getNim())) nama = m.getNama();
                        if(mkList != null) for(MataKuliah m : mkList) if(m.getKode().equals(n.getKodeMk())) mk = m.getNama();
                %>
                <tr style="border-bottom: 1px solid #f4f7f6;">
                    <td style="padding: 16px 20px;"><%= n.getNim() %></td>
                    <td style="padding: 16px 20px; font-weight: 500;"><%= nama %></td>
                    <td style="padding: 16px 20px;"><%= mk %></td>
                    <td style="padding: 16px 20px; text-align: center;"><%= n.getNilaiAngka() %></td>
                    <td style="padding: 16px 20px; text-align: center;">
                        <span style="background: #e8f5f2; color: var(--main-color); padding: 6px 14px; border-radius: 20px; font-weight: 700; font-size: 13px;">
                            <%= n.getGrade() %>
                        </span>
                    </td>
                </tr>
                <%  } 
                } else { %>
                <tr>
                    <td colspan="5" style="padding: 30px; text-align: center; color: var(--text-muted);">Belum ada data nilai yang diinput.</td>
                </tr>
                <% } %>
            </table>
        </div>
    </section>
</main>
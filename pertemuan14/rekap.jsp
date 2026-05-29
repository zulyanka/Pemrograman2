<%@ page import="com.app.model.*" %>
<%@ page import="java.util.List" %>
<jsp:include page="layout/header.jsp" />
<jsp:include page="layout/sidebar.jsp" />
<main class='main-content'>
    <section style="padding: 30px;">
        <h2>Rekapitulasi Nilai Mahasiswa</h2>
        <a href="rekap?export=pdf" class="btn btn-danger" style="display:inline-block; margin-bottom:15px;">
            <i class="fas fa-file-pdf"></i> Export ke PDF
        </a>
        <table>
            <tr><th>NIM</th><th>Nama Mahasiswa</th><th>Mata Kuliah</th><th>Nilai Angka</th><th>Grade</th></tr>
            <% 
                List<Nilai> nList = (List<Nilai>) request.getAttribute("nilaiList");
                List<Mahasiswa> mList = (List<Mahasiswa>) request.getAttribute("mhsList");
                List<MataKuliah> mkList = (List<MataKuliah>) request.getAttribute("mkList");

                if (nList != null) {
                    for (Nilai n : nList) {
                        String nama = "N/A"; String mk = "N/A";
                        if(mList != null) for(Mahasiswa m : mList) if(m.getNim().equals(n.getNim())) nama = m.getNama();
                        if(mkList != null) for(MataKuliah m : mkList) if(m.getKode().equals(n.getKodeMk())) mk = m.getNama();
            %>
            <tr>
                <td><%= n.getNim() %></td><td><%= nama %></td><td><%= mk %></td>
                <td><%= n.getNilaiAngka() %></td><td><strong><%= n.getGrade() %></strong></td>
            </tr>
            <% }} %>
        </table>
    </section>
</main>
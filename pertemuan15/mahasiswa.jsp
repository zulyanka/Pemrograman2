<%@ page import="com.app.model.Mahasiswa" %>
<%@ page import="java.util.List" %>
<jsp:include page="layout/header.jsp" />
<jsp:include page="layout/sidebar.jsp" />

<main class='main-content'>
    <section style="padding: 30px;">
        <h2>Kelola Data Mahasiswa</h2>
        
        <form action="mahasiswa" method="POST" style="margin-bottom: 20px; background: #fff; padding: 20px; border-radius: 8px;">
            <input type="text" name="nim" placeholder="NIM" required>
            <input type="text" name="nama" placeholder="Nama Mahasiswa" required>
            <input type="text" name="kelas" placeholder="Kelas" required>
            <input type="text" name="jurusan" placeholder="Jurusan" required>
            <button type="submit" class="btn btn-primary">Simpan Data</button>
        </form>

        <table>
            <tr><th>NIM</th><th>Nama</th><th>Kelas</th><th>Jurusan</th><th>Aksi</th></tr>
            <% 
                List<Mahasiswa> list = (List<Mahasiswa>) request.getAttribute("mahasiswaList");
                if (list != null) {
                    for (Mahasiswa m : list) {
            %>
            <tr>
                <td><%= m.getNim() %></td>
                <td><%= m.getNama() %></td>
                <td><%= m.getKelas() %></td>
                <td><%= m.getJurusan() %></td>
                <td>
                    <a href="mahasiswa?action=delete&nim=<%= m.getNim() %>" class="btn btn-danger" onclick="return confirm('Hapus data?');"><i class="fas fa-trash"></i></a>
                </td>
            </tr>
            <% }} %>
        </table>
    </section>
</main>
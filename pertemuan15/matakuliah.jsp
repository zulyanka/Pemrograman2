<%@ page import="com.app.model.MataKuliah" %>
<%@ page import="java.util.List" %>
<jsp:include page="layout/header.jsp" />
<jsp:include page="layout/sidebar.jsp" />
<main class='main-content'>
    <section style="padding: 30px;">
        <h2>Kelola Data Mata Kuliah</h2>
        <form action="matakuliah" method="POST" style="margin-bottom: 20px; background: #fff; padding: 20px; border-radius: 8px;">
            <input type="text" name="kode" placeholder="Kode MK" required>
            <input type="text" name="nama" placeholder="Nama Mata Kuliah" required>
            <input type="number" name="sks" placeholder="SKS" required>
            <button type="submit" class="btn btn-primary">Simpan Data</button>
        </form>
        <table>
            <tr><th>Kode</th><th>Mata Kuliah</th><th>SKS</th><th>Aksi</th></tr>
            <% 
                List<MataKuliah> list = (List<MataKuliah>) request.getAttribute("mkList");
                if (list != null) {
                    for (MataKuliah m : list) {
            %>
            <tr>
                <td><%= m.getKode() %></td><td><%= m.getNama() %></td><td><%= m.getSks() %></td>
                <td><a href="matakuliah?action=delete&kode=<%= m.getKode() %>" class="btn btn-danger">Hapus</a></td>
            </tr>
            <% }} %>
        </table>
    </section>
</main>
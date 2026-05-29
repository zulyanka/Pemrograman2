package com.app.util;

import com.app.model.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class PdfGenerator {
    public static void generateRekap(HttpServletResponse response, List<Nilai> nilaiList, List<Mahasiswa> mhsList, List<MataKuliah> mkList) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Rekap_Nilai.pdf");

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Header Identitas
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Rekapitulasi Nilai Mahasiswa", fontHeader));
            document.add(new Paragraph("Universitas Pamulang"));
            document.add(new Paragraph("Dicetak oleh: I Gede Yoga Setiawan (231011401028)"));
            document.add(new Paragraph(" ")); // Spasi

            // Tabel
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.addCell("NIM"); table.addCell("Nama Mahasiswa");
            table.addCell("Mata Kuliah"); table.addCell("Nilai"); table.addCell("Grade");

            for (Nilai n : nilaiList) {
                String nama = mhsList.stream().filter(m -> m.getNim().equals(n.getNim())).findFirst().map(Mahasiswa::getNama).orElse("N/A");
                String mk = mkList.stream().filter(m -> m.getKode().equals(n.getKodeMk())).findFirst().map(MataKuliah::getNama).orElse("N/A");
                
                table.addCell(n.getNim());
                table.addCell(nama);
                table.addCell(mk);
                table.addCell(String.valueOf(n.getNilaiAngka()));
                table.addCell(n.getGrade());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
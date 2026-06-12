package com.app.model;

public class Mahasiswa {
    private String nim;
    private String nama;
    private String kelas;
    private String jurusan;

    public Mahasiswa(String nim, String nama, String kelas, String jurusan) {
        this.nim = nim; this.nama = nama; this.kelas = kelas; this.jurusan = jurusan;
    }
    // Tambahkan Getter dan Setter
    public String getNim() { return nim; }
    public String getNama() { return nama; }
    public String getKelas() { return kelas; }
    public String getJurusan() { return jurusan; }
}
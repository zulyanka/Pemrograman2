package com.app.model;

public class Nilai {
    private String id;
    private String nim;
    private String kodeMk;
    private double nilaiAngka;
    private String grade;

    public Nilai(String id, String nim, String kodeMk, double nilaiAngka) {
        this.id = id; this.nim = nim; this.kodeMk = kodeMk; this.nilaiAngka = nilaiAngka;
        this.grade = hitungGrade(nilaiAngka);
    }
    
    private String hitungGrade(double nilai) {
        if (nilai >= 80) return "A";
        else if (nilai >= 70) return "B";
        else if (nilai >= 60) return "C";
        else if (nilai >= 50) return "D";
        else return "E";
    }

    public String getId() { return id; }
    public String getNim() { return nim; }
    public String getKodeMk() { return kodeMk; }
    public double getNilaiAngka() { return nilaiAngka; }
    public String getGrade() { return grade; }
}
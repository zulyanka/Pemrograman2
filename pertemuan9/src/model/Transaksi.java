// ============================================
// FILE    : model/Transaksi.java
// ============================================
package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private int idTransaksi;
    private String noNota;
    private int idCustomer;
    private String namaCustomer; // join
    private double totalHarga;
    private double bayar;
    private double kembalian;
    private Timestamp tanggal;
    private List<DetailTransaksi> details = new ArrayList<>();

    public Transaksi() {}

    // Getters & Setters
    public int getIdTransaksi()             { return idTransaksi; }
    public void setIdTransaksi(int v)       { this.idTransaksi = v; }

    public String getNoNota()               { return noNota; }
    public void setNoNota(String v)         { this.noNota = v; }

    public int getIdCustomer()              { return idCustomer; }
    public void setIdCustomer(int v)        { this.idCustomer = v; }

    public String getNamaCustomer()         { return namaCustomer; }
    public void setNamaCustomer(String v)   { this.namaCustomer = v; }

    public double getTotalHarga()           { return totalHarga; }
    public void setTotalHarga(double v)     { this.totalHarga = v; }

    public double getBayar()                { return bayar; }
    public void setBayar(double v)          { this.bayar = v; }

    public double getKembalian()            { return kembalian; }
    public void setKembalian(double v)      { this.kembalian = v; }

    public Timestamp getTanggal()           { return tanggal; }
    public void setTanggal(Timestamp v)     { this.tanggal = v; }

    public List<DetailTransaksi> getDetails()          { return details; }
    public void setDetails(List<DetailTransaksi> list) { this.details = list; }
}

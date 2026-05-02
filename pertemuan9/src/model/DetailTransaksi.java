// ============================================
// FILE    : model/DetailTransaksi.java
// ============================================
package model;

public class DetailTransaksi {
    private int idDetail;
    private int idTransaksi;
    private int idBarang;
    private String namaBarang;
    private double hargaJual;
    private int jumlah;
    private double subtotal;

    public DetailTransaksi() {}

    public DetailTransaksi(int idBarang, String namaBarang, double hargaJual, int jumlah) {
        this.idBarang  = idBarang;
        this.namaBarang = namaBarang;
        this.hargaJual = hargaJual;
        this.jumlah    = jumlah;
        this.subtotal  = hargaJual * jumlah;
    }

    public int getIdDetail()             { return idDetail; }
    public void setIdDetail(int v)       { this.idDetail = v; }

    public int getIdTransaksi()          { return idTransaksi; }
    public void setIdTransaksi(int v)    { this.idTransaksi = v; }

    public int getIdBarang()             { return idBarang; }
    public void setIdBarang(int v)       { this.idBarang = v; }

    public String getNamaBarang()        { return namaBarang; }
    public void setNamaBarang(String v)  { this.namaBarang = v; }

    public double getHargaJual()         { return hargaJual; }
    public void setHargaJual(double v)   { this.hargaJual = v; }

    public int getJumlah()               { return jumlah; }
    public void setJumlah(int v)         { this.jumlah = v; this.subtotal = this.hargaJual * v; }

    public double getSubtotal()          { return subtotal; }
    public void setSubtotal(double v)    { this.subtotal = v; }
}

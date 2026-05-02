// ============================================
// FILE    : model/Barang.java
// ============================================
package model;

/**
 * Entity class untuk tabel barang.
 */
public class Barang {
    private int idBarang;
    private String kodeBarang;
    private String namaBarang;
    private String kategori;
    private double hargaBeli;
    private double hargaJual;
    private int stok;
    private int idSupplier;
    private String namaSupplier; // join dari tabel supplier

    public Barang() {}

    public Barang(int idBarang, String kodeBarang, String namaBarang, String kategori,
                  double hargaBeli, double hargaJual, int stok, int idSupplier) {
        this.idBarang    = idBarang;
        this.kodeBarang  = kodeBarang;
        this.namaBarang  = namaBarang;
        this.kategori    = kategori;
        this.hargaBeli   = hargaBeli;
        this.hargaJual   = hargaJual;
        this.stok        = stok;
        this.idSupplier  = idSupplier;
    }

    // ---- Getters & Setters ----
    public int getIdBarang()           { return idBarang; }
    public void setIdBarang(int v)     { this.idBarang = v; }

    public String getKodeBarang()      { return kodeBarang; }
    public void setKodeBarang(String v){ this.kodeBarang = v; }

    public String getNamaBarang()      { return namaBarang; }
    public void setNamaBarang(String v){ this.namaBarang = v; }

    public String getKategori()        { return kategori; }
    public void setKategori(String v)  { this.kategori = v; }

    public double getHargaBeli()       { return hargaBeli; }
    public void setHargaBeli(double v) { this.hargaBeli = v; }

    public double getHargaJual()       { return hargaJual; }
    public void setHargaJual(double v) { this.hargaJual = v; }

    public int getStok()               { return stok; }
    public void setStok(int v)         { this.stok = v; }

    public int getIdSupplier()         { return idSupplier; }
    public void setIdSupplier(int v)   { this.idSupplier = v; }

    public String getNamaSupplier()    { return namaSupplier; }
    public void setNamaSupplier(String v) { this.namaSupplier = v; }

    @Override
    public String toString() { return namaBarang; }
}

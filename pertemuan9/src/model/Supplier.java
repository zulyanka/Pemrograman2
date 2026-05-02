// ============================================
// FILE    : model/Supplier.java
// ============================================
package model;

public class Supplier {
    private int idSupplier;
    private String namaSupplier;
    private String telepon;
    private String alamat;

    public Supplier() {}

    public Supplier(int idSupplier, String namaSupplier, String telepon, String alamat) {
        this.idSupplier   = idSupplier;
        this.namaSupplier = namaSupplier;
        this.telepon      = telepon;
        this.alamat       = alamat;
    }

    public int getIdSupplier()            { return idSupplier; }
    public void setIdSupplier(int v)      { this.idSupplier = v; }

    public String getNamaSupplier()       { return namaSupplier; }
    public void setNamaSupplier(String v) { this.namaSupplier = v; }

    public String getTelepon()            { return telepon; }
    public void setTelepon(String v)      { this.telepon = v; }

    public String getAlamat()             { return alamat; }
    public void setAlamat(String v)       { this.alamat = v; }

    @Override
    public String toString() { return namaSupplier; }
}

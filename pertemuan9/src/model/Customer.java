// ============================================
// FILE    : model/Customer.java
// ============================================
package model;

public class Customer {
    private int idCustomer;
    private String namaCustomer;
    private String telepon;
    private String alamat;

    public Customer() {}

    public Customer(int idCustomer, String namaCustomer, String telepon, String alamat) {
        this.idCustomer   = idCustomer;
        this.namaCustomer = namaCustomer;
        this.telepon      = telepon;
        this.alamat       = alamat;
    }

    public int getIdCustomer()            { return idCustomer; }
    public void setIdCustomer(int v)      { this.idCustomer = v; }

    public String getNamaCustomer()       { return namaCustomer; }
    public void setNamaCustomer(String v) { this.namaCustomer = v; }

    public String getTelepon()            { return telepon; }
    public void setTelepon(String v)      { this.telepon = v; }

    public String getAlamat()             { return alamat; }
    public void setAlamat(String v)       { this.alamat = v; }

    @Override
    public String toString() { return namaCustomer; }
}

// ================================================================
// File  : PlayStation.java
// Desc  : Model class (POJO) yang merepresentasikan data PlayStation
// ================================================================

public class PlayStation {

    private int id;
    private String namaKonsol;
    private String seriKonsol;
    private double hargaPerJam;
    private String status;
    private String keterangan;

    /** Constructor kosong */
    public PlayStation() {}

    /** Constructor dengan semua field */
    public PlayStation(int id, String namaKonsol, String seriKonsol,
                        double hargaPerJam, String status, String keterangan) {
        this.id = id;
        this.namaKonsol = namaKonsol;
        this.seriKonsol = seriKonsol;
        this.hargaPerJam = hargaPerJam;
        this.status = status;
        this.keterangan = keterangan;
    }

    // ======================
    // Getter dan Setter
    // ======================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaKonsol() {
        return namaKonsol;
    }

    public void setNamaKonsol(String namaKonsol) {
        this.namaKonsol = namaKonsol;
    }

    public String getSeriKonsol() {
        return seriKonsol;
    }

    public void setSeriKonsol(String seriKonsol) {
        this.seriKonsol = seriKonsol;
    }


    public double getHargaPerJam() {
        return hargaPerJam;
    }

    public void setHargaPerJam(double hargaPerJam) {
        this.hargaPerJam = hargaPerJam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public String toString() {
        return namaKonsol + " (" + seriKonsol + ")";
    }
}
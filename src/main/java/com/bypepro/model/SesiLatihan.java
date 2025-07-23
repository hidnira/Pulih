/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the data model for a single Exercise Session.
 * This class encapsulates a set of tasks assigned by a Therapist to a Patient,
 * including session details, a list of movements, the patient's report, and the therapist's evaluation.
 */

public class SesiLatihan {

    // Class Attributes
    private String namaSesi;
    private LocalDate tanggalTugas;
    private String status;
    private String catatanTerapis;
    private List<Gerakan> daftarGerakan;
    private Laporan laporanPasien;
    private Evaluasi evaluasiTerapis;


    /**
     * Constructs a new SesiLatihan object.
     * This is typically called when a therapist creates a new exercise program for a patient.
     * @param namaSesi The name or title of the exercise session (e.g., "Week 1: Warm-up").
     * @param tanggalTugas The date on which this session was assigned.
     * @param catatanTerapis An initial note or message from the therapist to the patient.
     */

    public SesiLatihan(String namaSesi, LocalDate tanggalTugas, String catatanTerapis) {
        this.namaSesi = namaSesi;
        this.tanggalTugas = tanggalTugas;
        this.catatanTerapis = catatanTerapis;
        this.status = "Menunggu Laporan";
        this.daftarGerakan = new ArrayList<>();
        this.laporanPasien = null;
        this.evaluasiTerapis = null;
    }


    // Getters and Setters
    public String getNamaSesi() {
        return namaSesi;
    }
    public void setNamaSesi(String namaSesi) {
        this.namaSesi = namaSesi;
    }
    public LocalDate getTanggalTugas() {
        return tanggalTugas;
    }
    public void setTanggalTugas(LocalDate tanggalTugas) {
        this.tanggalTugas = tanggalTugas;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCatatanTerapis() {
        return catatanTerapis;
    }
    public void setCatatanTerapis(String catatanTerapis) {
        this.catatanTerapis = catatanTerapis;
    }
    public List<Gerakan> getDaftarGerakan() {
        return daftarGerakan;
    }
    public void setDaftarGerakan(List<Gerakan> daftarGerakan) {
        this.daftarGerakan = daftarGerakan;
    }
    public Laporan getLaporanPasien() {
        return laporanPasien;
    }
    public void setLaporanPasien(Laporan laporanPasien) {
        this.laporanPasien = laporanPasien;
    }
    public Evaluasi getEvaluasiTerapis() {
        return evaluasiTerapis;
    }
    public void setEvaluasiTerapis(Evaluasi evaluasiTerapis) {
        this.evaluasiTerapis = evaluasiTerapis;
    }


    // Helper Method
    public void tambahGerakan(Gerakan gerakan) {
        this.daftarGerakan.add(gerakan);
    }
}
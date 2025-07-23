/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the data model for the Patient entity.
 * This class stores all personal, medical, and credential information for a patient,
 * as well as the list of their assigned exercise sessions. It is designed to work
 * with both XStream (using standard Java types) and JavaFX TableView (by providing property methods).
 */

public class Pasien {

    // Class Attributes
    private String id;
    private String namaLengkap;
    private String passwordHash;
    private String email;
    private String noTelepon;
    private LocalDate tanggalLahir;
    private String jenisKelamin;
    private String diagnosisUtama;
    private LocalDate tanggalPembuatanAkun;

    private List<SesiLatihan> daftarSesiLatihan;


    /**
     * Constructs a new Pasien object with all its details.
     * This is typically called when a therapist registers a new patient.
     * @param id The unique identifier for the patient.
     * @param namaLengkap The patient's full name.
     * @param passwordHash The patient's hashed password.
     * @param email The patient's email address.
     * @param noTelepon The patient's phone number.
     * @param tanggalLahir The patient's date of birth.
     * @param jenisKelamin The patient's gender.
     * @param diagnosisUtama The patient's primary diagnosis.
     * @param tanggalPembuatanAkun The date the patient's account was created.
     */

    public Pasien(String id, String namaLengkap, String passwordHash, String email, String noTelepon, LocalDate tanggalLahir, String jenisKelamin, String diagnosisUtama, LocalDate tanggalPembuatanAkun) {
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.passwordHash = passwordHash;
        this.email = email;
        this.noTelepon = noTelepon;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.diagnosisUtama = diagnosisUtama;
        this.tanggalPembuatanAkun = tanggalPembuatanAkun;
        this.daftarSesiLatihan = new ArrayList<>();
    }


    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }

    public LocalDate getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(LocalDate tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getDiagnosisUtama() { return diagnosisUtama; }
    public void setDiagnosisUtama(String diagnosisUtama) { this.diagnosisUtama = diagnosisUtama; }

    public LocalDate getTanggalPembuatanAkun() { return tanggalPembuatanAkun; }
    public void setTanggalPembuatanAkun(LocalDate tanggalPembuatanAkun) { this.tanggalPembuatanAkun = tanggalPembuatanAkun; }

    public List<SesiLatihan> getDaftarSesiLatihan() { return daftarSesiLatihan; }
    public void setDaftarSesiLatihan(List<SesiLatihan> daftarSesiLatihan) { this.daftarSesiLatihan = daftarSesiLatihan; }


    // Methods for TableView
    public void tambahSesiLatihan(SesiLatihan sesi) { this.daftarSesiLatihan.add(sesi); }
    public StringProperty idProperty() { return new SimpleStringProperty(id); }
    public StringProperty namaLengkapProperty() { return new SimpleStringProperty(namaLengkap); }
    public StringProperty diagnosisUtamaProperty() { return new SimpleStringProperty(diagnosisUtama); }
    public ObjectProperty<LocalDate> tanggalLahirProperty() { return new SimpleObjectProperty<>(tanggalLahir); }
}
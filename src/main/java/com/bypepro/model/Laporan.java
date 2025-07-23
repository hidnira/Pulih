/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

/**
 * Represents the data model for an Exercise Report.
 * This class encapsulates the data submitted by a Patient as proof of completion
 * for a specific exercise session.
 */

public class Laporan {

    // Class Attributes
    private String pathVideo;
    private int skalaNyeri;
    private String catatanPasien;


    /**
     * Constructs a new Laporan (Report) object.
     * This is typically called when a patient submits their exercise report.
     * @param pathVideo The absolute path to the uploaded video file.
     * @param skalaNyeri The pain level (1-5) reported by the patient.
     * @param catatanPasien Additional notes from the patient regarding the session.
     */

    public Laporan(String pathVideo, int skalaNyeri, String catatanPasien) {
        this.pathVideo = pathVideo;
        this.skalaNyeri = skalaNyeri;
        this.catatanPasien = catatanPasien;
    }


    // Getters and Setters
    public String getPathVideo() {
        return pathVideo;
    }
    public void setPathVideo(String pathVideo) {
        this.pathVideo = pathVideo;
    }

    public int getSkalaNyeri() {
        return skalaNyeri;
    }
    public void setSkalaNyeri(int skalaNyeri) {
        this.skalaNyeri = skalaNyeri;
    }

    public String getCatatanPasien() {
        return catatanPasien;
    }
    public void setCatatanPasien(String catatanPasien) {
        this.catatanPasien = catatanPasien;
    }
}
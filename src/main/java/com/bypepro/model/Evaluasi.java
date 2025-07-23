/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

/**
 * Represents the data model for an Evaluation provided by a Therapist.
 * This class encapsulates the feedback and final status given by a therapist
 * after reviewing a patient's exercise report.
 */

public class Evaluasi {

    // Class Attributes
    private String umpanBalik;
    private String statusAkhir;


    /**
     * Constructs a new Evaluation object.
     * @param umpanBalik The feedback text or message from the Physiotherapist.
     * @param statusAkhir The final status of the exercise session (e.g., "Perlu Perbaikan").
     */

    public Evaluasi(String umpanBalik, String statusAkhir) {
        this.umpanBalik = umpanBalik;
        this.statusAkhir = statusAkhir;
    }


    // Getters and Setters
    public String getUmpanBalik() {
        return umpanBalik;
    }
    public void setUmpanBalik(String umpanBalik) {
        this.umpanBalik = umpanBalik;
    }

    public String getStatusAkhir() {
        return statusAkhir;
    }
    public void setStatusAkhir(String statusAkhir) {
        this.statusAkhir = statusAkhir;
    }
}
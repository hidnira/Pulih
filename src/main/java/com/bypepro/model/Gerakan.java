/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

/**
 * Represents the simple data model for a single exercise Movement
 * within an exercise session (SesiLatihan).
 */

public class Gerakan {

    // Class Attributes
    private String namaGerakan;
    private String instruksi;


    /**
     * Constructs a new Gerakan object.
     * @param namaGerakan The name of the movement (e.g., "Plank").
     * @param instruksi The execution details for the movement (e.g., "3 Sets x 30 Seconds").
     */

    public Gerakan(String namaGerakan, String instruksi) {
        this.namaGerakan = namaGerakan;
        this.instruksi = instruksi;
    }


    // Getters and Setters
    public String getNamaGerakan() { return namaGerakan; }
    public void setNamaGerakan(String namaGerakan) {
        this.namaGerakan = namaGerakan;
    }

    public String getInstruksi() {
        return instruksi;
    }
    public void setInstruksi(String instruksi) {
        this.instruksi = instruksi;
    }
}
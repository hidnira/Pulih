/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the top-level data container for the entire application.
 * This class acts as a wrapper for all primary data lists, designed to be the
 * single root object for XML serialization with XStream, which simplifies the save/load process.
 */

public class PulihData {

    // Class Attributes
    private List<Fisioterapis> listFisioterapis;
    private List<Pasien> listPasien;


    /**
     * Default constructor for PulihData.
     * Initializes the main data lists as empty ArrayLists to prevent
     * NullPointerExceptions, especially on the application's first run.
     */

    public PulihData() {
        listFisioterapis = new ArrayList<>();
        listPasien = new ArrayList<>();
    }


    // Getters and Setters
    public List<Fisioterapis> getListFisioterapis() {
        return listFisioterapis;
    }
    public void setListFisioterapis(List<Fisioterapis> listFisioterapis) {
        this.listFisioterapis = listFisioterapis;
    }

    public List<Pasien> getListPasien() {
        return listPasien;
    }
    public void setListPasien(List<Pasien> listPasien) {
        this.listPasien = listPasien;
    }
}
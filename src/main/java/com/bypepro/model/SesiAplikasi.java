/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

/**
 * A Singleton class designed to manage the application's session state.
 * Its primary role is to hold a reference to the currently logged-in user,
 * providing a global access point to this information from anywhere in the application.
 */

public class SesiAplikasi {

    // Singleton Implementation
    private static SesiAplikasi instance;
    private Fisioterapis loggedInTerapis;


    // Private constructor to prevent instantiation from other classes.
    private SesiAplikasi() {}


    /**
     * Provides the single, global instance of the SesiAplikasi class.
     * @return The singleton instance of SesiAplikasi.
     */

    public static synchronized SesiAplikasi getInstance() {
        if (instance == null) {
            instance = new SesiAplikasi();
        }
        return instance;
    }


    // Session Management Methods
    public Fisioterapis getLoggedInTerapis() {
        return loggedInTerapis;
    }
    public void setLoggedInTerapis(Fisioterapis loggedInTerapis) {
        this.loggedInTerapis = loggedInTerapis;
    }
    public void clearSession() {
        this.loggedInTerapis = null;
    }
}
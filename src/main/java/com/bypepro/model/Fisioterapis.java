/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.model;

/**
 * Represents the data model for the Physiotherapist entity.
 * This class is responsible for storing data related to the Therapist user,
 * primarily their credentials for authentication purposes.
 */

public class Fisioterapis {

    // Class Attributes
    private String username;
    private String passwordHash;
    private String namaLengkap;
    private String email;
    private String noTelepon;


    /**
     * Constructs a new Fisioterapis object.
     * @param username The username for login.
     * @param passwordHash The hashed password for storage.
     * @param namaLengkap The full name of the therapist.
     * @param email The email address of the therapist.
     * @param noTelepon The phone number of the therapist.
     */

    public Fisioterapis(String username, String passwordHash, String namaLengkap, String email, String noTelepon) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.noTelepon = noTelepon;
    }


    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
}
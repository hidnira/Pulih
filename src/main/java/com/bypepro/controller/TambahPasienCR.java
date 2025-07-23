/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.Pasien;
import com.bypepro.model.PasswordHasher;
import com.bypepro.model.PulihData;
import com.bypepro.model.XMLDataManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Controller for the "Add/Edit Patient" pop-up window.
 * This class can operate in two modes: "Add Mode" for creating a new patient,
 * and "Edit Mode" for updating an existing patient's data.
 */

public class TambahPasienCR implements Initializable {

    // FXML Component Declarations
    @FXML private TextField namaField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker tanggalLahirPicker;
    @FXML private TextField diagnosisField;
    @FXML private TextField emailField;
    @FXML private TextField teleponField;
    @FXML private ComboBox<String> jenisKelaminComboBox;
    @FXML private Button simpanButton;
    @FXML private Button kembaliButton;
    @FXML private Label errorLabel;

    private Pasien pasienUntukDiedit;
    private boolean modeEdit = false;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");


    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up initial UI states,
     * such as ComboBox items and input field listeners for real-time validation.
     * @param location The location used to resolve relative paths for the root object, or null if not known.
     * @param resources The resources used to localize the root object, or null if not known.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jenisKelaminComboBox.getItems().addAll("Laki-Laki", "Perempuan");

        teleponField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    teleponField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    /**
     * Initializes the form for editing an existing patient.
     * This method is called from the previous screen to pre-populate all form fields
     * with the selected patient's data.
     * @param pasien The Patient object to be edited.
     */

    public void initDataUntukEdit(Pasien pasien) {
        this.pasienUntukDiedit = pasien;
        this.modeEdit = true;

        namaField.setText(pasien.getNamaLengkap());
        emailField.setText(pasien.getEmail());
        teleponField.setText(pasien.getNoTelepon());
        tanggalLahirPicker.setValue(pasien.getTanggalLahir());
        jenisKelaminComboBox.setValue(pasien.getJenisKelamin());
        diagnosisField.setText(pasien.getDiagnosisUtama());
        passwordField.setText(pasien.getPasswordHash());
    }


    // Event Handlers

    /**
     * Handles the "Simpan" (Save) button click.
     * It performs multiple validations (required fields, email format, duplicate name),
     * and then either updates an existing patient record or creates a new one
     * based on the form's mode, before saving to the database and closing the window.
     */

    @FXML
    private void handleSimpan() {
        String email = emailField.getText();
        String namaPasienBaru = namaField.getText().trim();

        if (namaPasienBaru.isEmpty() || passwordField.getText().isEmpty() || tanggalLahirPicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Lengkap", "Data Belum Lengkap", "Nama, Password, dan Tanggal Lahir wajib diisi.");
            return;
        }

        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            showAlert(Alert.AlertType.ERROR, "Format Salah", "Format Email Tidak Valid", "Harap masukkan format email yang benar (contoh: user@example.com).");
            return;
        }

        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();

        boolean namaDuplikat = false;
        for (Pasien p : dataAplikasi.getListPasien()) {
            if (p.getNamaLengkap().equalsIgnoreCase(namaPasienBaru)) {
                if (modeEdit && p.getId().equals(pasienUntukDiedit.getId())) {
                    continue;
                }
                namaDuplikat = true;
                break;
            }
        }

        if (namaDuplikat) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", "Nama Pasien Duplikat", "Nama pasien '" + namaPasienBaru + "' sudah terdaftar.");
            return;
        }

        if (modeEdit) {
            for (Pasien p : dataAplikasi.getListPasien()) {
                if (p.getId().equals(pasienUntukDiedit.getId())) {
                    p.setNamaLengkap(namaPasienBaru);
                    p.setEmail(email);
                    p.setNoTelepon(teleponField.getText());
                    p.setTanggalLahir(tanggalLahirPicker.getValue());
                    p.setJenisKelamin(jenisKelaminComboBox.getValue());
                    p.setDiagnosisUtama(diagnosisField.getText());

                    if (!passwordField.getText().equals(p.getPasswordHash())) {
                        String newHashedPassword = PasswordHasher.hashPassword(passwordField.getText());
                        p.setPasswordHash(newHashedPassword);
                    }
                    break;
                }
            }
        } else {
            String idUnik = "PULIH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String hashedPassword = PasswordHasher.hashPassword(passwordField.getText());
            Pasien pasienBaru = new Pasien(
                    idUnik,
                    namaPasienBaru,
                    hashedPassword,
                    email,
                    teleponField.getText(),
                    tanggalLahirPicker.getValue(),
                    jenisKelaminComboBox.getValue(),
                    diagnosisField.getText(),
                    LocalDate.now()
            );
            dataAplikasi.getListPasien().add(pasienBaru);
        }

        XMLDataManager.getInstance().saveData(dataAplikasi);
        closeWindow();
    }


    /**
     * Handles the "Kembali" (Back) button click. Closes the window.
     */

    @FXML
    private void handleKembali() {
        closeWindow();
    }


    // Private Helper Methods

    /**
     * A helper method to close the current pop-up window (Stage).
     */

    private void closeWindow() {
        Stage stage = (Stage) simpanButton.getScene().getWindow();
        stage.close();
    }


    /**
     * A helper method to display an Alert dialog.
     * @param alertType The type of alert (e.g., ERROR, INFORMATION).
     * @param title The title of the alert window.
     * @param header The header text of the alert.
     * @param content The main content message of the alert.
     */

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
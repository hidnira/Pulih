/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for the "Evaluate Exercise" pop-up window.
 * This class is responsible for displaying a patient's report,
 * providing a form for the therapist to submit feedback,
 * and a button to open the report video in an external player.
 */

public class EvaluasiLatihanCR implements Initializable {

    // FXML Component Declarations
    @FXML private Label namaPasienLabel;
    @FXML private Label namaSesiLabel;
    @FXML private Label tanggalTugasLabel;
    @FXML private VBox daftarGerakanVBox;
    @FXML private Label skalaNyeriLabel;
    @FXML private Label catatanPasienLabel;
    @FXML private Label videoPathLabel;
    @FXML private TextArea umpanBalikArea;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button simpanButton;
    @FXML private Button kembaliButton;
    @FXML private Button bukaVideoButton;

    private Pasien targetPasien;
    private SesiLatihan targetSesi;
    private File videoFile;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the initial state of UI components.
     * @param url The location used to resolve relative paths for the root object, or null if not known.
     * @param resourceBundle The resources used to localize the root object, or null if not known.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusComboBox.getItems().addAll("Gerakan Benar & Selesai", "Perlu Perbaikan", "Minta Pasien Mengulang");
        statusComboBox.setValue("Gerakan Benar & Selesai");
    }


    /**
     * Receives data from the previous screen (PasienDetailCR).
     * This method populates the view with the details of the selected patient and exercise session.
     * @param pasien The patient whose report is being evaluated.
     * @param sesi The specific exercise session to be evaluated.
     */

    public void initData(Pasien pasien, SesiLatihan sesi) {
        this.targetPasien = pasien;
        this.targetSesi = sesi;

        namaPasienLabel.setText(pasien.getNamaLengkap());
        namaSesiLabel.setText(sesi.getNamaSesi());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
        tanggalTugasLabel.setText(sesi.getTanggalTugas().format(formatter));

        daftarGerakanVBox.getChildren().clear();
        for (Gerakan gerakan : sesi.getDaftarGerakan()) {
            daftarGerakanVBox.getChildren().add(new Label("• " + gerakan.getNamaGerakan() + ": " + gerakan.getInstruksi()));
        }

        if (sesi.getLaporanPasien() != null) {
            Laporan laporan = sesi.getLaporanPasien();
            skalaNyeriLabel.setText(laporan.getSkalaNyeri() + " dari 5");
            catatanPasienLabel.setText(laporan.getCatatanPasien());

            this.videoFile = new File(laporan.getPathVideo());
            if (videoFile.exists()) {
                videoPathLabel.setText(videoFile.getName());
                bukaVideoButton.setDisable(false);
            } else {
                videoPathLabel.setText("File video tidak ditemukan.");
                bukaVideoButton.setDisable(true);
            }
        } else {
            catatanPasienLabel.setText("Pasien belum mengirimkan laporan untuk sesi ini.");
            umpanBalikArea.setDisable(true);
            simpanButton.setDisable(true);
            bukaVideoButton.setDisable(true);
        }
    }


    // Event Handlers

    /**
     * Handles the action of the "Buka Video" button click.
     * Opens the patient's report video using the system's default media player.
     */

    @FXML
    private void handleBukaVideo() {
        if (Desktop.isDesktopSupported() && videoFile != null && videoFile.exists()) {
            try {
                Desktop.getDesktop().open(videoFile);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Gagal Membuka Video", "Tidak dapat membuka file video.", "Pastikan ada aplikasi pemutar video yang terinstal di komputermu.");
            }
        }
    }


    /**
     * Handles the action of the "Buka Video" button click.
     * Opens the patient's report video using the system's default media player.
     */

    @FXML
    private void handleSimpan() {
        Evaluasi evaluasiBaru = new Evaluasi(umpanBalikArea.getText(), statusComboBox.getValue());
        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
        for (Pasien p : dataAplikasi.getListPasien()) {
            if (p.getId().equals(targetPasien.getId())) {
                for (SesiLatihan s : p.getDaftarSesiLatihan()) {
                    if (s.getNamaSesi().equals(targetSesi.getNamaSesi())) {
                        s.setEvaluasiTerapis(evaluasiBaru);
                        s.setStatus("Sudah Dievaluasi");
                        break;
                    }
                }
                break;
            }
        }
        XMLDataManager.getInstance().saveData(dataAplikasi);
        closeWindow();
    }


    /**
     * Handles the action of the "Kembali" (Back) button click.
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
        Stage stage = (Stage) kembaliButton.getScene().getWindow();
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
/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controller for the Exercise Session Detail screen, from the Patient's perspective.
 * This class is responsible for displaying all details of a selected exercise session,
 * including the list of movements, notes from the therapist, and evaluation results if available.
 */

public class SesiDetailCR {

    // FXML Component Declarations
    @FXML private Label namaSesiLabel;
    @FXML private Label namaTerapisLabel;
    @FXML private Label tanggalTugasLabel;
    @FXML private Label catatanTerapisLabel;
    @FXML private VBox daftarGerakanVBox;
    @FXML private VBox evaluasiBox;
    @FXML private Label statusEvaluasiLabel;
    @FXML private Label umpanBalikLabel;
    @FXML private Button kirimLaporanButton;
    @FXML private Button kembaliButton;
    @FXML private Text laporkanProgresLabel;
    @FXML private Text hasilEvaluasiText;

    private Pasien pasienLogin;
    private SesiLatihan sesiLatihan;


    /**
     * Receives data from the previous screen (PasienDashboardCR).
     * This method populates the view with the details of the selected patient and exercise session.
     * @param pasien The currently logged-in patient.
     * @param sesi The selected exercise session to display.
     */

    public void initData(Pasien pasien, SesiLatihan sesi) {
        this.pasienLogin = pasien;
        this.sesiLatihan = sesi;

        namaSesiLabel.setText(sesi.getNamaSesi());
        catatanTerapisLabel.setText(sesi.getCatatanTerapis());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
        tanggalTugasLabel.setText(sesi.getTanggalTugas().format(formatter));

        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
        if (!dataAplikasi.getListFisioterapis().isEmpty()) {
            Fisioterapis terapis = dataAplikasi.getListFisioterapis().get(0);
            namaTerapisLabel.setText(terapis.getNamaLengkap());
        } else {
            namaTerapisLabel.setText("Terapis Tidak Ditemukan");
        }

        daftarGerakanVBox.getChildren().clear();
        for (Gerakan gerakan : sesi.getDaftarGerakan()) {
            daftarGerakanVBox.getChildren().add(new Label("• " + gerakan.getNamaGerakan() + ": " + gerakan.getInstruksi()));
        }

        if ("Menunggu Laporan".equals(sesi.getStatus())) {
            evaluasiBox.setVisible(false);
            kirimLaporanButton.setVisible(true);
            laporkanProgresLabel.setVisible(true);
            hasilEvaluasiText.setVisible(false);
        } else if (sesi.getEvaluasiTerapis() != null) {
            evaluasiBox.setVisible(true);
            kirimLaporanButton.setVisible(false);
            Evaluasi evaluasi = sesi.getEvaluasiTerapis();
            statusEvaluasiLabel.setText(evaluasi.getStatusAkhir());
            umpanBalikLabel.setText(evaluasi.getUmpanBalik());
            hasilEvaluasiText.setVisible(true);
            laporkanProgresLabel.setVisible(false);
        } else {
            evaluasiBox.setVisible(false);
            kirimLaporanButton.setVisible(false);
            laporkanProgresLabel.setVisible(false);
            hasilEvaluasiText.setVisible(false);
        }
    }


    // Event Handlers

    /**
     * Handles the action for the "Kirim Laporan" (Submit Report) button.
     * Opens the report submission form in a new pop-up window.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleKirimLaporan() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/LaporkanLatihanView.fxml"));
        Parent root = loader.load();
        LaporkanLatihanCR controller = loader.getController();
        controller.initData(pasienLogin, sesiLatihan);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Laporan untuk: " + sesiLatihan.getNamaSesi());
        stage.setScene(new Scene(root));
        stage.showAndWait();

        kembaliKeDashboard();
    }


    /**
     * Handles the action for the "Kembali" (Back) button.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleKembali() throws IOException {
        kembaliKeDashboard();
    }


    // Private Helper Methods

    /**
     * A helper method to navigate back to the Patient Dashboard screen.
     * This is used to avoid code duplication.
     * @throws IOException if the FXML file is not found.
     */

    private void kembaliKeDashboard() throws IOException {
        Stage stage = (Stage) kembaliButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/PasienDashboardView.fxml"));
        Parent root = loader.load();
        PasienDashboardCR controller = loader.getController();
        controller.initData(pasienLogin);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
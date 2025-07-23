/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Controller for the "Report Exercise" pop-up window.
 * This class handles the form where patients submit their progress,
 * including video uploads, pain scale input, and additional notes.
 */

public class LaporkanLatihanCR {

    // FXML Component Declarations
    @FXML private Label videoPathLabel;
    @FXML private Slider nyeriSlider;
    @FXML private TextArea catatanArea;
    @FXML private Button kirimButton;

    private Pasien targetPasien;
    private SesiLatihan targetSesi;
    private File videoFile;


    /**
     * Receives data from the previous screen (e.g., PasienDashboardCR).
     * This method sets the context for which patient and session the report is for.
     * @param pasien The patient who is submitting the report.
     * @param sesi The exercise session being reported on.
     */

    public void initData(Pasien pasien, SesiLatihan sesi) {
        this.targetPasien = pasien;
        this.targetSesi = sesi;
    }


    // Event Handlers

    /**
     * Handles the action of the "Pilih File Video" button click.
     * Opens a native file chooser dialog for the user to select their report video.
     */

    @FXML
    private void handlePilihVideo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Video Laporan");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.avi")
        );
        File selectedFile = fileChooser.showOpenDialog(kirimButton.getScene().getWindow());

        if (selectedFile != null) {
            this.videoFile = selectedFile;
            videoPathLabel.setText(selectedFile.getName());
        }
    }


    /**
     * Handles the action of the "Kirim Laporan" button click.
     * It validates the input, copies the video file into the project directory
     * with a unique name, creates a new Report object, updates the session status
     * to "Perlu Evaluasi", saves the data, and closes the window.
     * @throws IOException if there is an error during file copy operation.
     */

    @FXML
    private void handleKirimLaporan() throws IOException {
        if (videoFile == null) {
            videoPathLabel.setText("Video wajib diunggah!");
            videoPathLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Path folderTujuan = Paths.get("videos/" + targetPasien.getId());
        Files.createDirectories(folderTujuan);

        String namaAsli = videoFile.getName();
        String namaTanpaEkstensi = namaAsli.substring(0, namaAsli.lastIndexOf('.'));
        String ekstensi = namaAsli.substring(namaAsli.lastIndexOf('.'));

        Path pathBaru = folderTujuan.resolve(namaAsli);
        int counter = 2;
        while (Files.exists(pathBaru)) {
            String namaBaru = namaTanpaEkstensi + "(" + counter + ")" + ekstensi;
            pathBaru = folderTujuan.resolve(namaBaru);
            counter++;
        }

        Files.copy(videoFile.toPath(), pathBaru, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Video berhasil disalin ke: " + pathBaru.toAbsolutePath());

        Laporan laporanBaru = new Laporan(
                pathBaru.toString(),
                (int) nyeriSlider.getValue(),
                catatanArea.getText()
        );

        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
        for (Pasien p : dataAplikasi.getListPasien()) {
            if (p.getId().equals(targetPasien.getId())) {
                for (SesiLatihan s : p.getDaftarSesiLatihan()) {
                    if (s.getNamaSesi().equals(targetSesi.getNamaSesi())) {
                        s.setLaporanPasien(laporanBaru);
                        s.setStatus("Perlu Evaluasi");
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
     * Handles the action of the "Kembali" button click.
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
        Stage stage = (Stage) kirimButton.getScene().getWindow();
        stage.close();
    }
}
/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Controller for the "Add/Edit Exercise" pop-up window.
 * This class operates in two modes: "Add Mode" for creating a new session,
 * and "Edit Mode" for updating an existing one.
 */

public class TambahLatihanCR {

    // FXML Component Declarations
    @FXML private TextField namaSesiField;
    @FXML private DatePicker tanggalTugasPicker;
    @FXML private VBox daftarGerakanVBox;
    @FXML private TextArea catatanArea;
    @FXML private Button simpanButton;
    @FXML private Button kembaliButton;
    @FXML private Button hapusLatihanButton;

    private Pasien targetPasien;
    private SesiLatihan sesiUntukDiedit;
    private boolean modeEdit = false;


    /**
     * Initializes the form for "Add Mode".
     * @param pasien The patient for whom a new exercise session will be created.
     */

    public void initData(Pasien pasien) {
        this.targetPasien = pasien;
        this.modeEdit = false;
        hapusLatihanButton.setVisible(false);
        tambahBarisGerakan("", "");
    }


    /**
     * Initializes the form for "Edit Mode".
     * Populates all form fields with data from an existing exercise session.
     * @param pasien The patient who owns the session.
     * @param sesi The exercise session to be edited.
     */

    public void initDataUntukEdit(Pasien pasien, SesiLatihan sesi) {
        this.targetPasien = pasien;
        this.sesiUntukDiedit = sesi;
        this.modeEdit = true;

        namaSesiField.setText(sesi.getNamaSesi());
        tanggalTugasPicker.setValue(sesi.getTanggalTugas());
        catatanArea.setText(sesi.getCatatanTerapis());

        for (Gerakan gerakan : sesi.getDaftarGerakan()) {
            tambahBarisGerakan(gerakan.getNamaGerakan(), gerakan.getInstruksi());
        }

        if ("Menunggu Laporan".equals(sesi.getStatus())) {
            hapusLatihanButton.setVisible(true);
        } else {
            hapusLatihanButton.setVisible(false);
        }
    }


    // Event Handlers

    /**
     * Handles the "+ Tambah Gerakan" button click.
     * Calls a helper method to dynamically add a new empty row for an exercise movement.
     */

    @FXML
    private void handleTambahGerakan() {
        tambahBarisGerakan("", "");
    }


    /**
     * Handles the "Simpan" (Save) button click.
     * Validates for duplicate session names, then either updates the existing session
     * or creates a new one based on the current mode.
     */

    @FXML
    private void handleSimpanLatihan() {
        String namaSesiBaru = namaSesiField.getText().trim();

        boolean namaDuplikat = false;
        for (SesiLatihan sesi : targetPasien.getDaftarSesiLatihan()) {
            if (sesi.getNamaSesi().equalsIgnoreCase(namaSesiBaru)) {
                if (modeEdit && sesi.getNamaSesi().equalsIgnoreCase(sesiUntukDiedit.getNamaSesi())) {
                    continue;
                }
                namaDuplikat = true;
                break;
            }
        }

        if (namaDuplikat) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", "Nama Sesi Duplikat",
                    "Nama sesi latihan '" + namaSesiBaru + "' sudah ada untuk pasien ini. Harap gunakan nama lain.");
            return;
        }

        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
        if (modeEdit) {
            for (Pasien p : dataAplikasi.getListPasien()) {
                if (p.getId().equals(targetPasien.getId())) {
                    for (SesiLatihan s : p.getDaftarSesiLatihan()) {
                        if (s.getNamaSesi().equals(sesiUntukDiedit.getNamaSesi())) {
                            s.setNamaSesi(namaSesiBaru);
                            s.setTanggalTugas(tanggalTugasPicker.getValue());
                            s.setCatatanTerapis(catatanArea.getText());
                            s.getDaftarGerakan().clear();
                            for (Node node : daftarGerakanVBox.getChildren()) {
                                if (node instanceof HBox) {
                                    HBox baris = (HBox) node;
                                    TextField nama = (TextField) baris.getChildren().get(0);
                                    TextField instruksi = (TextField) baris.getChildren().get(1);
                                    if (!nama.getText().isEmpty()) {
                                        s.tambahGerakan(new Gerakan(nama.getText(), instruksi.getText()));
                                    }
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        } else {
            SesiLatihan sesiBaru = new SesiLatihan(namaSesiBaru, tanggalTugasPicker.getValue(), catatanArea.getText());
            for (Node node : daftarGerakanVBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox baris = (HBox) node;
                    TextField nama = (TextField) baris.getChildren().get(0);
                    TextField instruksi = (TextField) baris.getChildren().get(1);
                    if (!nama.getText().isEmpty()) {
                        sesiBaru.tambahGerakan(new Gerakan(nama.getText(), instruksi.getText()));
                    }
                }
            }
            for (Pasien p : dataAplikasi.getListPasien()) {
                if (p.getId().equals(targetPasien.getId())) {
                    p.tambahSesiLatihan(sesiBaru);
                    break;
                }
            }
        }

        XMLDataManager.getInstance().saveData(dataAplikasi);
        closeWindow();
    }


    /**
     * Handles the "Hapus Latihan" (Delete Exercise) button click.
     * Shows a confirmation dialog and deletes the session if confirmed.
     */

    @FXML
    private void handleHapusLatihan() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus Latihan");
        alert.setHeaderText("Anda akan menghapus sesi: " + sesiUntukDiedit.getNamaSesi());
        alert.setContentText("Aksi ini tidak dapat dibatalkan. Lanjutkan?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
            for (Pasien p : dataAplikasi.getListPasien()) {
                if (p.getId().equals(targetPasien.getId())) {
                    p.getDaftarSesiLatihan().removeIf(s -> s.getNamaSesi().equals(sesiUntukDiedit.getNamaSesi()));
                    break;
                }
            }
            XMLDataManager.getInstance().saveData(dataAplikasi);
            closeWindow();
        }
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
     * A helper method to dynamically create and add the UI components for a single exercise movement row.
     * @param nama The initial name of the movement (for edit mode).
     * @param instruksi The initial instruction for the movement (for edit mode).
     */

    private void tambahBarisGerakan(String nama, String instruksi) {
        HBox barisGerakan = new HBox(10);
        TextField namaGerakanField = new TextField(nama);
        namaGerakanField.setPromptText("Nama Gerakan");
        namaGerakanField.setPrefWidth(200);
        TextField instruksiField = new TextField(instruksi);
        instruksiField.setPromptText("Instruksi (Contoh: 3 Set x 10 Repetisi)");
        instruksiField.setPrefWidth(300);
        Button hapusButton = new Button("X");
        hapusButton.setOnAction(e -> daftarGerakanVBox.getChildren().remove(barisGerakan));
        barisGerakan.getChildren().addAll(namaGerakanField, instruksiField, hapusButton);
        daftarGerakanVBox.getChildren().add(barisGerakan);
    }


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
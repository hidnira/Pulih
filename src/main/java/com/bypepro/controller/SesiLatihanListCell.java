/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.Pasien;
import com.bypepro.model.PulihData;
import com.bypepro.model.SesiLatihan;
import com.bypepro.model.XMLDataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A custom ListCell for displaying SesiLatihan objects.
 * This class is responsible for rendering a single SesiLatihan object
 * within a ListView, using the layout defined in SesiLatihanCell.fxml.
 */

public class SesiLatihanListCell extends ListCell<SesiLatihan> {

    // FXML Component Declarations
    @FXML private Label namaSesiLabel;
    @FXML private Label tanggalTugasLabel;
    @FXML private Label statusLabel;
    @FXML private Button editButton;
    @FXML private GridPane gridPane;

    private FXMLLoader mLLoader;
    private Pasien pasien;
    private String userRole;


    /**
     * Constructs a new SesiLatihanListCell.
     * @param pasien The patient who owns this session. This is needed for context in edit/delete actions.
     * @param role The role of the current user ("pasien" or "terapis") to control UI visibility.
     */

    public SesiLatihanListCell(Pasien pasien, String role) {
        this.pasien = pasien;
        this.userRole = role;
    }


    /**
     * This is the core method of the ListCell, overridden from the base class.
     * JavaFX calls this method whenever a cell needs to be displayed or updated (e.g., during scrolling).
     * @param sesi The SesiLatihan object to be rendered in this cell.
     * @param empty True if the cell is empty and should not display any data.
     */

    @Override
    protected void updateItem(SesiLatihan sesi, boolean empty) {
        super.updateItem(sesi, empty);

        if (empty || sesi == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/com/bypepro/view/SesiLatihanCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            namaSesiLabel.setText(sesi.getNamaSesi());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
            tanggalTugasLabel.setText("Tugas pada: " + sesi.getTanggalTugas().format(formatter));
            statusLabel.setText(sesi.getStatus());

            if ("terapis".equals(userRole) && "Menunggu Laporan".equals(sesi.getStatus())) {
                editButton.setVisible(true);
            } else {
                editButton.setVisible(false);
            }

            switch (sesi.getStatus()) {
                case "Perlu Evaluasi":
                    statusLabel.setStyle("-fx-text-fill: #ffa500; -fx-font-weight: bold;");
                    break;
                case "Sudah Dievaluasi":
                    statusLabel.setStyle("-fx-text-fill: #2e8b57; -fx-font-weight: bold;");
                    break;
                default:
                    statusLabel.setStyle("-fx-text-fill: #595959;");
                    break;
            }

            setText(null);
            setGraphic(gridPane);
        }
    }


    // Event Handlers

    /**
     * Handles the action for the "Edit" button click, triggered by the therapist.
     * Opens the "Add/Edit Exercise" form in edit mode.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleEdit() throws IOException {
        SesiLatihan sesiUntukDiedit = getItem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/TambahLatihanView.fxml"));
        Parent root = loader.load();

        TambahLatihanCR controller = loader.getController();
        controller.initDataUntukEdit(pasien, sesiUntukDiedit);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Latihan: " + sesiUntukDiedit.getNamaSesi());
        stage.setScene(new Scene(root));
        stage.showAndWait();

        System.out.println("Form edit/hapus ditutup, me-refresh daftar latihan...");
        PulihData dataAplikasiTerbaru = XMLDataManager.getInstance().loadData();
        for (Pasien p : dataAplikasiTerbaru.getListPasien()) {
            if (p.getId().equals(this.pasien.getId())) {
                getListView().getItems().setAll(p.getDaftarSesiLatihan());
                break;
            }
        }
    }
}
/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.Fisioterapis;
import com.bypepro.model.Pasien;
import com.bypepro.model.SesiAplikasi;
import com.bypepro.model.SesiLatihan;
import com.bypepro.model.XMLDataManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for the main Therapist Dashboard screen.
 * This class is responsible for displaying summary data, a list of all patients,
 * and serves as the navigation hub for therapist-specific features.
 */

public class TerapisDashboardCR implements Initializable {

    // FXML Component Declarations
    @FXML private TableView<Pasien> pasienTableView;
    @FXML private TableColumn<Pasien, String> namaColumn;
    @FXML private TableColumn<Pasien, String> diagnosisColumn;
    @FXML private TableColumn<Pasien, String> statusColumn;
    @FXML private TableColumn<Pasien, Void> aksiColumn;
    @FXML private Label totalPasienLabel;
    @FXML private Label perluTinjauanLabel;
    @FXML private Label jadwalHariIniLabel;
    @FXML private Label kepatuhanLabel;
    @FXML private Label sapaanTerapisLabel;

    private final ObservableList<Pasien> daftarPasien = FXCollections.observableArrayList();


    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It sets up the initial state of the view.
     * @param location The location used to resolve relative paths for the root object, or null if not known.
     * @param resources The resources used to localize the root object, or null if not known.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Fisioterapis terapis = SesiAplikasi.getInstance().getLoggedInTerapis();
        if (terapis != null) {
            sapaanTerapisLabel.setText("Selamat Datang Kembali, " + terapis.getNamaLengkap() + "!");
        }

        namaColumn.setCellValueFactory(cellData -> cellData.getValue().namaLengkapProperty());
        diagnosisColumn.setCellValueFactory(cellData -> cellData.getValue().diagnosisUtamaProperty());

        statusColumn.setCellValueFactory(cellData -> {
            Pasien pasien = cellData.getValue();
            String statusTerakhir = "Belum Ada Latihan";

            if (pasien.getDaftarSesiLatihan() != null && !pasien.getDaftarSesiLatihan().isEmpty()) {
                SesiLatihan sesiTerakhir = pasien.getDaftarSesiLatihan().stream()
                        .max((s1, s2) -> s1.getTanggalTugas().compareTo(s2.getTanggalTugas()))
                        .orElse(null);
                if (sesiTerakhir != null) {
                    statusTerakhir = sesiTerakhir.getStatus();
                }
            }
            return new SimpleStringProperty(statusTerakhir);
        });

        setupAksiColumn();
        refreshData();
    }


    // Event Handlers

    /**
     * Handles the action for the "+ Tambah Pasien Baru" button click.
     * Opens the "Add New Patient" form in a new pop-up window.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleTambahPasien() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/TambahPasienView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Tambah Pasien Baru");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        refreshData();
    }


    /**
     * Handles the action for the "Keluar" (Logout) button click.
     * Clears the current user session and navigates back to the role selection screen.
     * @param event The action event from the button click.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        SesiAplikasi.getInstance().clearSession();
        Parent root = FXMLLoader.load(getClass().getResource("/com/bypepro/view/RoleSelectionView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    // Private Helper Methods

    /**
     * Reloads all data from the XML file and updates the UI.
     * This ensures the view is always synchronized with the latest data.
     */

    private void refreshData() {
        daftarPasien.setAll(XMLDataManager.getInstance().loadData().getListPasien());
        pasienTableView.setItems(daftarPasien);
        updateStatistik();
    }


    /**
     * Calculates all statistics for the dashboard cards and updates the corresponding labels.
     */

    private void updateStatistik() {
        totalPasienLabel.setText((String.valueOf(daftarPasien.size())) + " Orang");

        int perluTinjauan = 0;
        int jadwalHariIni = 0;
        int totalSesiDitugaskan = 0;
        int totalSesiSelesai = 0;
        LocalDate hariIni = LocalDate.now();

        for (Pasien pasien : daftarPasien) {
            for (SesiLatihan sesi : pasien.getDaftarSesiLatihan()) {
                totalSesiDitugaskan++;
                if ("Perlu Evaluasi".equals(sesi.getStatus())) {
                    perluTinjauan++;
                }
                if (sesi.getTanggalTugas().isEqual(hariIni)) {
                    jadwalHariIni++;
                }
                if ("Sudah Dievaluasi".equals(sesi.getStatus())) {
                    totalSesiSelesai++;
                }
            }
        }

        perluTinjauanLabel.setText((String.valueOf(perluTinjauan)) + " Laporan");
        jadwalHariIniLabel.setText((String.valueOf(jadwalHariIni)) + " Sesi");

        if (totalSesiDitugaskan > 0) {
            double kepatuhan = ((double) totalSesiSelesai / totalSesiDitugaskan) * 100;
            kepatuhanLabel.setText(String.format("%.0f%%", kepatuhan));
        } else {
            kepatuhanLabel.setText("N/A");
        }
    }


    /**
     * Sets up the "Aksi" (Action) column to create and display a "Lihat Detail" button
     * in each row of the patient table.
     */

    private void setupAksiColumn() {
        Callback<TableColumn<Pasien, Void>, TableCell<Pasien, Void>> cellFactory = param -> {
            return new TableCell<>() {
                private final Button btn = new Button("Lihat Detail");
                {
                    btn.setOnAction((ActionEvent event) -> {
                        Pasien pasien = getTableView().getItems().get(getIndex());
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/PasienDetailView.fxml"));
                            Parent root = loader.load();
                            PasienDetailCR controller = loader.getController();
                            controller.initData(pasien);

                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
        };
        aksiColumn.setCellFactory(cellFactory);
    }
}
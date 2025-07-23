/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for the main Patient Dashboard screen.
 * This class is responsible for displaying a summary of the patient's data,
 * the currently active exercise session ("Today's Focus"), the history of all sessions,
 * and handling navigation to other features from the patient's perspective.
 */

public class PasienDashboardCR implements Initializable {

    // FXML Component Declarations
    @FXML private Label sapaanLabel;
    @FXML private Label fokusNamaSesiLabel;
    @FXML private VBox fokusGerakanDetailVBox;
    @FXML private ListView<SesiLatihan> sesiLatihanListView;
    @FXML private Label terdiagnosaLabel;
    @FXML private Label totalLatihanLabel;
    @FXML private Label terakhirLatihanLabel;
    @FXML private Label namaLengkapLabel;
    @FXML private Label idPasienLabel;
    @FXML private Label diagnosisUtamaLabel;

    private Pasien pasienLogin;
    private SesiLatihan sesiFokus;
    private final ObservableList<SesiLatihan> daftarSesiLatihan = FXCollections.observableArrayList();


    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It binds the observable list to the ListView.
     * @param location The location used to resolve relative paths for the root object, or null if not known.
     * @param resourceBundle The resources used to localize the root object, or null if not known.
     */

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        sesiLatihanListView.setItems(daftarSesiLatihan);
    }


    /**
     * Receives the logged-in patient's data from the LoginCR.
     * This is the main entry point for populating the dashboard with data.
     * @param pasien The Pasien object for the currently logged-in user.
     */

    public void initData(Pasien pasien) {
        this.pasienLogin = pasien;

        sesiLatihanListView.setCellFactory(listView -> new SesiLatihanListCell(this.pasienLogin, "pasien"));
        refreshUI();
    }


    // Event Handlers

    /**
     * Handles the action for the "Laporkan Selesai" (Report Completion) button.
     * It opens the report submission form for the currently focused exercise session.
     * @throws IOException if the FXML file for the report form is not found.
     */

    @FXML
    private void handleLaporkanSelesai() throws IOException {
        if (sesiFokus != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/LaporkanLatihanView.fxml"));
            Parent root = loader.load();
            LaporkanLatihanCR controller = loader.getController();
            controller.initData(pasienLogin, sesiFokus);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Laporan untuk: " + sesiFokus.getNamaSesi());
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshUI();
        } else {
            System.out.println("Tidak ada sesi latihan yang perlu dilaporkan.");
        }
    }


    /**
     * Handles the action for the "Hubungi Terapis" (Contact Therapist) button.
     * Attempts to open a WhatsApp chat with the therapist using the default web browser.
     */

    @FXML
    private void handleHubungiTerapis() {
        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();

        if (!dataAplikasi.getListFisioterapis().isEmpty()) {
            Fisioterapis terapis = dataAplikasi.getListFisioterapis().get(0);
            String noTelepon = terapis.getNoTelepon();

            if (noTelepon != null && !noTelepon.isEmpty()) {
                if (noTelepon.startsWith("0")) {
                    noTelepon = "62" + noTelepon.substring(1);
                }
                String urlWhatsApp = "https://wa.me/" + noTelepon;

                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI(urlWhatsApp));
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal membuka browser.", "Terjadi kesalahan saat mencoba membuka WhatsApp.");
                    }
                }
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Informasi Kontak", "Nomor telepon terapis tidak tersedia.", "Anda dapat menghubungi terapis melalui Email: " + terapis.getEmail());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Informasi Kontak", "Data terapis tidak ditemukan.", "");
        }
    }


    /**
     * Handles the action for double-clicking an item in the session ListView.
     * Navigates to the detailed view for the selected session.
     * @param event The mouse event from the click.
     * @throws IOException if the FXML file for the session detail view is not found.
     */

    @FXML
    private void handleSesiListClicked(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            SesiLatihan sesiTerpilih = sesiLatihanListView.getSelectionModel().getSelectedItem();
            if (sesiTerpilih != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/SesiDetailView.fxml"));
                Parent root = loader.load();
                SesiDetailCR controller = loader.getController();
                controller.initData(pasienLogin, sesiTerpilih);

                Stage stage = (Stage) sesiLatihanListView.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }


    /**
     * Handles the action for the "Keluar" (Logout) button.
     * Navigates the user back to the initial role selection screen.
     * @param event The action event from the button click.
     * @throws IOException if the FXML file for the role selection view is not found.
     */

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/bypepro/view/RoleSelectionView.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    // Private Helper Methods

    /**
     * A helper method to reload data from the XML file and update all UI components.
     * This ensures the view is always synchronized with the latest data.
     */

    private void refreshUI() {
        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
        for (Pasien p : dataAplikasi.getListPasien()) {
            if (p.getId().equals(pasienLogin.getId())) {
                this.pasienLogin = p;
                break;
            }
        }

        namaLengkapLabel.setText(pasienLogin.getNamaLengkap());
        idPasienLabel.setText(pasienLogin.getId());
        diagnosisUtamaLabel.setText(pasienLogin.getDiagnosisUtama());
        sapaanLabel.setText("Lanjutkan Latihanmu, " + pasienLogin.getNamaLengkap() + "!");

        daftarSesiLatihan.setAll(pasienLogin.getDaftarSesiLatihan());

        updateInfoCards();

        this.sesiFokus = null;
        fokusNamaSesiLabel.setText("Tidak ada tugas baru");
        fokusGerakanDetailVBox.getChildren().clear();
        for (SesiLatihan sesi : pasienLogin.getDaftarSesiLatihan()) {
            if ("Menunggu Laporan".equals(sesi.getStatus())) {
                this.sesiFokus = sesi;
                fokusNamaSesiLabel.setText(sesi.getNamaSesi());
                for (Gerakan gerakan : sesi.getDaftarGerakan()) {
                    fokusGerakanDetailVBox.getChildren().add(
                            new Label("• " + gerakan.getNamaGerakan() + ": " + gerakan.getInstruksi())
                    );
                }
                break;
            }
        }
    }


    /**
     * A helper method called by refreshUI to calculate and display
     * the data in the three statistic cards.
     */

    private void updateInfoCards() {
        List<SesiLatihan> semuaSesi = pasienLogin.getDaftarSesiLatihan();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));

        totalLatihanLabel.setText(semuaSesi.size() + " Sesi");

        if (pasienLogin.getTanggalPembuatanAkun() != null) {
            terdiagnosaLabel.setText(pasienLogin.getTanggalPembuatanAkun().format(formatter));
        } else {
            terdiagnosaLabel.setText("-");
        }

        SesiLatihan sesiTerakhirDilaporkan = semuaSesi.stream()
                .filter(sesi -> sesi.getLaporanPasien() != null)
                .max(Comparator.comparing(SesiLatihan::getTanggalTugas))
                .orElse(null);
        if (sesiTerakhirDilaporkan != null) {
            terakhirLatihanLabel.setText(sesiTerakhirDilaporkan.getTanggalTugas().format(formatter));
        } else {
            terakhirLatihanLabel.setText("-");
        }
    }


    /**
     * A helper method to display an Alert dialog consistently.
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
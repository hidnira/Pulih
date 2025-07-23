/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.Pasien;
import com.bypepro.model.PulihData;
import com.bypepro.model.SesiLatihan;
import com.bypepro.model.XMLDataManager;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the Patient Detail screen, from the Therapist's perspective.
 * This view provides a comprehensive overview of a single patient, including
 * personal data, statistics, rehabilitation progress via a chart,
 * and a list of all assigned exercise sessions.
 */

public class PasienDetailCR implements Initializable {

    // FXML Component Declarations
    @FXML private Label namaPasienLabel;
    @FXML private Label idPasienLabel;
    @FXML private Label diagnosisLabel;
    @FXML private Label emailLabel;
    @FXML private Label teleponLabel;
    @FXML private Label tanggalLahirLabel;
    @FXML private Label jenisKelaminLabel;
    @FXML private Label terdiagnosaLabel;
    @FXML private Label totalLatihanLabel;
    @FXML private Label terakhirLatihanLabel;
    @FXML private ListView<SesiLatihan> sesiLatihanListView;
    @FXML private LineChart<String, Number> kepatuhanChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ComboBox<String> rentangWaktuComboBox;
    @FXML private Button hapusAkunButton;
    @FXML private Button hubungiPasienButton;
    @FXML private Button tambahLatihanButton;
    @FXML private Button kembaliButton;
    @FXML private Button editPasienButton;

    private Pasien pasien;
    private final ObservableList<SesiLatihan> daftarSesiLatihan = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     * Sets up UI components that do not depend on external data, like the ComboBox options.
     * @param location The location used to resolve relative paths.
     * @param resources The resources used to localize the root object.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sesiLatihanListView.setItems(daftarSesiLatihan);
        rentangWaktuComboBox.getItems().addAll("1 Bulan Terakhir", "3 Bulan Terakhir", "6 Bulan Terakhir", "Semua Waktu");
        rentangWaktuComboBox.setValue("Semua Waktu");
    }


    /**
     * Receives the selected Patient object from the previous screen (TerapisDashboardCR).
     * This is the main entry point for populating the view with data.
     * @param pasien The Patient object whose details will be displayed.
     */

    public void initData(Pasien pasien) {
        this.pasien = pasien;
        sesiLatihanListView.setCellFactory(listView -> new SesiLatihanListCell(this.pasien, "terapis"));
        refreshHalamanDetail();
    }


    // Event Handlers

    /**
     * Handles the action for the "Kembali" (Back) button.
     * @param event The action event from the button click.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
        kembaliKeDashboard(kembaliButton);
    }


    /**
     * Handles the action for the "Hapus Akun" (Delete Account) button.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleHapusAkun() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus Akun");
        alert.setHeaderText("Anda akan menghapus akun pasien: " + pasien.getNamaLengkap());
        alert.setContentText("Aksi ini tidak dapat dibatalkan. Lanjutkan?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
            dataAplikasi.getListPasien().removeIf(p -> p.getId().equals(this.pasien.getId()));
            XMLDataManager.getInstance().saveData(dataAplikasi);
            kembaliKeDashboard(hapusAkunButton);
        }
    }


    /**
     * Handles the action for the "+ Tambah Latihan" (Add Exercise) button.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleTambahLatihan() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/TambahLatihanView.fxml"));
        Parent root = loader.load();
        TambahLatihanCR controller = loader.getController();
        controller.initData(this.pasien);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Tambah Latihan untuk " + pasien.getNamaLengkap());
        stage.setScene(new Scene(root));
        stage.showAndWait();
        refreshHalamanDetail();
    }


    /**
     * Handles the action for the "Hubungi Pasien" (Contact Patient) button.
     */

    @FXML
    private void handleHubungiPasien() {
        if (this.pasien != null && this.pasien.getNoTelepon() != null && !this.pasien.getNoTelepon().isEmpty()) {
            String noTelepon = this.pasien.getNoTelepon();
            if (noTelepon.startsWith("0")) {
                noTelepon = "62" + noTelepon.substring(1);
            }
            String urlWhatsApp = "https://wa.me/" + noTelepon;
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(urlWhatsApp));
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gagal");
                    alert.setHeaderText("Gagal membuka browser.");
                    alert.setContentText("Terjadi kesalahan saat mencoba membuka WhatsApp.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Informasi Tidak Lengkap");
            alert.setHeaderText("Nomor telepon pasien tidak ditemukan.");
            alert.showAndWait();
        }
    }


    /**
     * Handles the action when a new time range is selected in the ComboBox.
     * @param event The action event from the ComboBox selection.
     */

    @FXML
    private void handleRentangWaktuChange(ActionEvent event) {
        String pilihan = rentangWaktuComboBox.getValue();
        if (pilihan == null) return;
        switch (pilihan) {
            case "1 Bulan Terakhir": setupKepatuhanChart(1); break;
            case "3 Bulan Terakhir": setupKepatuhanChart(3); break;
            case "6 Bulan Terakhir": setupKepatuhanChart(6); break;
            default: setupKepatuhanChart(0); break;
        }
    }


    /**
     * Handles the action for the "Edit Pasien" button.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleEditPasien() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/TambahPasienView.fxml"));
        Parent root = loader.load();
        TambahPasienCR controller = loader.getController();
        controller.initDataUntukEdit(this.pasien);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Data Pasien");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        refreshHalamanDetail();
    }


    /**
     * Handles double-click events on the session ListView.
     * Navigates to the evaluation screen if the session status is "Perlu Evaluasi".
     * @param event The mouse event from the click.
     * @throws IOException if the FXML file is not found.
     */

    @FXML
    private void handleSesiListClicked(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            SesiLatihan sesiTerpilih = sesiLatihanListView.getSelectionModel().getSelectedItem();
            if (sesiTerpilih != null && "Perlu Evaluasi".equals(sesiTerpilih.getStatus())) {
                bukaJendelaEvaluasi(sesiTerpilih);
            }
        }
    }


    // Private Helper Methods

    /**
     * A helper method to reload all data for the current patient from the XML file and update all UI components.
     * This ensures the view is always synchronized with the latest data after any modifications.
     */

    private void refreshHalamanDetail() {
        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
        for (Pasien p : dataAplikasi.getListPasien()) {
            if (p.getId().equals(this.pasien.getId())) {
                this.pasien = p;
                break;
            }
        }

        updateTampilanDataPasien();

        daftarSesiLatihan.setAll(this.pasien.getDaftarSesiLatihan());
        updateInfoCards();
        handleRentangWaktuChange(null);
    }


    /**
     * Populates the labels displaying the patient's personal and contact information.
     */

    private void updateTampilanDataPasien() {
        namaPasienLabel.setText(pasien.getNamaLengkap());
        idPasienLabel.setText(pasien.getId());
        diagnosisLabel.setText(pasien.getDiagnosisUtama());
        emailLabel.setText(pasien.getEmail());
        teleponLabel.setText(pasien.getNoTelepon());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
        tanggalLahirLabel.setText(pasien.getTanggalLahir().format(formatter));
        jenisKelaminLabel.setText(pasien.getJenisKelamin());
    }


    /**
     * Calculates and displays the data in the three statistic cards.
     */

    private void updateInfoCards() {
        List<SesiLatihan> semuaSesi = this.pasien.getDaftarSesiLatihan();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
        totalLatihanLabel.setText(semuaSesi.size() + " Sesi");
        if (this.pasien.getTanggalPembuatanAkun() != null) {
            terdiagnosaLabel.setText(this.pasien.getTanggalPembuatanAkun().format(formatter));
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
     * Sets up and draws the adherence LineChart based on the patient's session data
     * and the selected time range.
     * @param rentangBulan The number of past months to display data for (0 for all time).
     */

    private void setupKepatuhanChart(int rentangBulan) {
        List<SesiLatihan> sesiUntukChart;
        if (rentangBulan > 0) {
            LocalDate batasWaktu = LocalDate.now().minusMonths(rentangBulan);
            sesiUntukChart = pasien.getDaftarSesiLatihan().stream()
                    .filter(sesi -> sesi.getTanggalTugas().isAfter(batasWaktu))
                    .collect(Collectors.toList());
        } else {
            sesiUntukChart = pasien.getDaftarSesiLatihan();
        }

        Map<String, List<SesiLatihan>> sesiPerMinggu = sesiUntukChart.stream()
                .collect(Collectors.groupingBy(sesi -> {
                    String[] parts = sesi.getNamaSesi().split(" ");
                    if (parts.length >= 2) return parts[0] + " " + parts[1].replaceAll("[:(]", "");
                    return sesi.getNamaSesi();
                }));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tingkat Kepatuhan");
        TreeMap<String, List<SesiLatihan>> sortedSesi = new TreeMap<>(Comparator.comparingInt(minggu -> {
            String nomorMingguStr = minggu.replaceAll("[^0-9]", "");
            return nomorMingguStr.isEmpty() ? 0 : Integer.parseInt(nomorMingguStr);
        }));
        sortedSesi.putAll(sesiPerMinggu);

        for (Map.Entry<String, List<SesiLatihan>> entry : sortedSesi.entrySet()) {
            List<SesiLatihan> sesiDiMingguIni = entry.getValue();
            long totalSesi = sesiDiMingguIni.size();
            long sesiSelesai = sesiDiMingguIni.stream().filter(s -> "Sudah Dievaluasi".equals(s.getStatus())).count();
            double kepatuhan = (totalSesi > 0) ? ((double) sesiSelesai / totalSesi) * 100 : 0;
            series.getData().add(new XYChart.Data<>(entry.getKey(), kepatuhan));
        }
        yAxis.setLabel("Persentase (%)");
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(100);
        kepatuhanChart.getData().clear();
        kepatuhanChart.getData().add(series);
    }


    /**
     * A helper method to open the "Evaluate Exercise" pop-up window.
     * @param sesi The exercise session to be evaluated.
     * @throws IOException if the FXML file is not found.
     */

    private void bukaJendelaEvaluasi(SesiLatihan sesi) throws IOException {
        SesiLatihan sesiUntukEvaluasi = sesi;
        sesiLatihanListView.getSelectionModel().clearSelection();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/EvaluasiLatihanView.fxml"));
        Parent root = loader.load();
        EvaluasiLatihanCR controller = loader.getController();
        controller.initData(this.pasien, sesiUntukEvaluasi);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Evaluasi Latihan untuk " + sesiUntukEvaluasi.getNamaSesi());
        stage.setScene(new Scene(root));
        stage.showAndWait();
        refreshHalamanDetail();
    }


    /**
     * A helper method to navigate back to the Therapist Dashboard.
     * @param node A UI component (like a button) to get the current window.
     * @throws IOException if the FXML file is not found.
     */

    private void kembaliKeDashboard(Node node) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/bypepro/view/TerapisDashboardView.fxml"));
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
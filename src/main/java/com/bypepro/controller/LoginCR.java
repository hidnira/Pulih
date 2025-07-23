/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import com.bypepro.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the unified login screen.
 * This class handles the authentication process for both Patient and Therapist roles,
 * based on the role data received from the previous screen.
 */

public class LoginCR {

    // FXML Component Declarations
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Label welcomeLabel;

    private String userRole;
    private Pasien pasienYangLogin;
    private Fisioterapis terapisYangLogin;

    /**
     * Initializes the controller with data from the previous screen.
     * This method sets the context for the login form, updating UI elements
     * like prompts and labels based on the user's selected role.
     * @param role The user role ("pasien" or "terapis").
     */

    public void initData(String role) {
        this.userRole = role;
        if ("pasien".equals(role)) {
            welcomeLabel.setText("Halo Pasien!");
            usernameField.setPromptText("Email kamu");
        } else {
            welcomeLabel.setText("Halo Terapis!");
            usernameField.setPromptText("Username kamu");
        }
    }


    // Event Handlers

    /**
     * Handles the action of the "Masuk" (Login) button click.
     * This method validates user input, checks credentials against the XML database
     * based on the user role, and navigates to the appropriate dashboard upon success.
     * @param event The action event from the button click.
     * @throws IOException If the dashboard FXML file is not found.
     */

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        String usernameOrEmail = usernameField.getText();
        String password = passwordField.getText();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Input tidak boleh kosong!");
            return;
        }

        PulihData dataAplikasi = XMLDataManager.getInstance().loadData();
        boolean loginSukses = false;

        if ("terapis".equals(userRole)) {
            for (Fisioterapis terapis : dataAplikasi.getListFisioterapis()) {
                String inputPasswordHashed = PasswordHasher.hashPassword(password);
                if (terapis.getUsername().equals(usernameOrEmail) && terapis.getPasswordHash().equals(inputPasswordHashed)) {
                    loginSukses = true;
                    this.terapisYangLogin = terapis;
                    break;
                }
            }
        } else if ("pasien".equals(userRole)) {
            for (Pasien pasien : dataAplikasi.getListPasien()) {
                String inputPasswordHashed = PasswordHasher.hashPassword(password);
                if (pasien.getEmail().equals(usernameOrEmail) && pasien.getPasswordHash().equals(inputPasswordHashed)) {
                    loginSukses = true;
                    this.pasienYangLogin = pasien;
                    break;
                }
            }
        }

        if (loginSukses) {
            System.out.println("LOGIN BERHASIL sebagai " + userRole);
            if ("terapis".equals(userRole)) {
                SesiAplikasi.getInstance().setLoggedInTerapis(this.terapisYangLogin);

                changeScene(event, "/com/bypepro/view/TerapisDashboardView.fxml");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/PasienDashboardView.fxml"));
                Parent root = loader.load();
                PasienDashboardCR controller = loader.getController();
                controller.initData(pasienYangLogin);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
        } else {
            System.out.println("LOGIN GAGAL! Kredensial salah.");
            errorLabel.setText("Kredensial salah!");
        }
    }


    /**
     * Handles the action of the "Kembali" (Back) button click.
     * Navigates the user back to the role selection screen.
     * @param event The action event from the button click.
     * @throws IOException If the FXML file is not found.
     */

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        changeScene(event, "/com/bypepro/view/RoleSelectionView.fxml");
    }


    // Private Helper Methods

    /**
     * A helper method to switch scenes.
     * This is used for simple navigation that does not require passing data to the next controller.
     * @param event The action event that triggered the scene change.
     * @param fxmlFile The path to the destination FXML file.
     * @throws IOException If the FXML file is not found.
     */

    private void changeScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
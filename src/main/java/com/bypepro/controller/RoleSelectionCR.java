/**
 * Created by Pemula Produktif 2025
 */

package com.bypepro.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Role Selection screen.
 * This is the first screen the user interacts with, allowing them to choose
 * whether to proceed as a Patient or a Therapist.
 */

public class RoleSelectionCR {

    // Event Handlers

    /**
     * Handles the action for the "Pasien" (Patient) button click.
     * Initiates navigation to the login screen with the "pasien" role.
     * @param event The action event from the button click.
     * @throws IOException if the LoginView FXML file cannot be found.
     */

    @FXML
    public void handlePasienButton(ActionEvent event) throws IOException {
        changeToLoginScene(event, "pasien");
    }


    /**
     * Handles the action for the "Terapis" (Therapist) button click.
     * Initiates navigation to the login screen with the "terapis" role.
     * @param event The action event from the button click.
     * @throws IOException if the LoginView FXML file cannot be found.
     */

    @FXML
    public void handleTerapisButton(ActionEvent event) throws IOException {
        changeToLoginScene(event, "terapis");
    }


    // Private Helper Methods

    /**
     * A helper method to navigate to the login screen.
     * This method loads the login FXML, gets its controller, passes the selected role,
     * and then replaces the current scene on the active window.
     * @param event The action event that triggered the scene change.
     * @param role The role string ("pasien" or "terapis") to be sent to the LoginCR.
     * @throws IOException if the LoginView FXML file cannot be found.
     */

    private void changeToLoginScene(ActionEvent event, String role) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bypepro/view/LoginView.fxml"));
        Parent root = loader.load();

        LoginCR loginController = loader.getController();

        loginController.initData(role);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
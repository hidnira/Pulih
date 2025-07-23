/**
 * Project Pulih
 * Created by Pemula Produktif
 * Last Update on 2025
 */

package com.bypepro;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * This method is called after the application has been launched.
     * It sets up the initial loading screen and then transitions to the main application view.
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException if the FXML file for the loading screen cannot be found.
     */

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent loadingRoot = FXMLLoader.load(getClass().getResource("view/LoadingView.fxml"));
        Scene loadingScene = new Scene(loadingRoot, 800, 520);

        Image icon = new Image(getClass().getResourceAsStream("view/images/icon-pulihh.png"));
        primaryStage.getIcons().add(icon);

        primaryStage.setTitle("Pulih by Pemula Produktif");

        primaryStage.setScene(loadingScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        PauseTransition jeda = new PauseTransition(Duration.seconds(3));
        jeda.setOnFinished(event -> {
            try {
                Parent mainRoot = FXMLLoader.load(getClass().getResource("view/RoleSelectionView.fxml"));
                Scene mainScene = new Scene(mainRoot, 800, 520);

                primaryStage.setScene(mainScene);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        jeda.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
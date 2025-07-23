module com.bypepro {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;
    requires xstream;

    opens com.bypepro.model to xstream;
    opens com.bypepro to javafx.fxml;
    opens com.bypepro.controller to javafx.fxml;

    exports com.bypepro;
}
module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.logging; // <-- indispensable pour Logger

    opens classeur.fichier to javafx.fxml;
    exports classeur.fichier;
}
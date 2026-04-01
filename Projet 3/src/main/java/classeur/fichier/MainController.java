package classeur.fichier;

import java.io.File;
import java.nio.file.Path;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button btnChoose;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private ListView<String> logList;

    private FileWatcher watcher;
    private Path selectedPath;

    @FXML
    private void chooseFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir un dossier à surveiller");

        File folder = chooser.showDialog(new Stage());

        if (folder != null) {
            selectedPath = folder.toPath();
            logList.getItems().add("Dossier sélectionné : " + selectedPath);
        }
    }

    @FXML
    private void startWatching() {
        if (selectedPath == null) {
            logList.getItems().add("Veuillez choisir un dossier d'abord.");
            return;
        }

        watcher = new FileWatcher(selectedPath, logList);
        watcher.start();

        logList.getItems().add("Surveillance démarrée.");
    }

    @FXML
    private void stopWatching() {
        if (watcher != null) {
            watcher.stopWatching();
            logList.getItems().add("Surveillance arrêtée.");
        }
    }
}
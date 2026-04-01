package classeur.fichier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("main.fxml"));

        stage.setScene(new Scene(loader.load(), 500, 400));
        stage.setTitle("Auto File Sorter");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
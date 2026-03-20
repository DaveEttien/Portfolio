package devoir3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        GestionnaireMotos gestionnaire = new GestionnaireMotos();
        MainView root = new MainView(gestionnaire);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Gestionnaire de Motos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

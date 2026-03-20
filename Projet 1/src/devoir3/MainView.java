package devoir3;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {

    public MainView(GestionnaireMotos gestionnaire) {

        VBox menu = new VBox(15);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #2c3e50;");

        Button btnAjouter = new Button("Ajouter une moto");
        Button btnListe = new Button("Afficher les motos");
        Button btnSupprimer = new Button("Supprimer une moto");
        Button btnAugmenter = new Button("Augmenter kilométrage");
        Button btnComparer = new Button("Comparer deux motos");
        Button btnAjouter4 = new Button("Ajouter 4 motos prédéfinies");

        Button[] boutons = {btnAjouter, btnListe, btnSupprimer, btnAugmenter, btnComparer, btnAjouter4};

        for (Button b : boutons) {
            b.setPrefWidth(200);
            b.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        }

        menu.getChildren().addAll(boutons);
        setLeft(menu);

        btnAjouter.setOnAction(e -> setCenter(new AjouterMotoView(gestionnaire)));
        btnListe.setOnAction(e -> setCenter(new ListeMotosView(gestionnaire)));
        btnSupprimer.setOnAction(e -> setCenter(new SupprimerMotoView(gestionnaire)));
        btnAugmenter.setOnAction(e -> setCenter(new AugmenterKmView(gestionnaire)));
        btnComparer.setOnAction(e -> setCenter(new ComparerMotosView(gestionnaire)));
        btnAjouter4.setOnAction(e -> setCenter(new AjouterQuatreMotosView(gestionnaire)));
    }
}

package devoir3;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class InterfaceMotos extends StackPane {

    private GestionnaireMotos gestionnaire;

    // Tous les écrans
    private VBox ecranMenu;
    private VBox ecranListe;
    private VBox ecranAjouter;
    private VBox ecranSupprimer;
    private VBox ecranAugmenter;
    private VBox ecranComparer;
    private VBox ecranAjouter4;

    public InterfaceMotos(GestionnaireMotos gestionnaire) {
        this.gestionnaire = gestionnaire;

        // Création des écrans
        ecranMenu = creerEcranMenu();
        ecranListe = creerEcranListe();
        ecranAjouter = creerEcranAjouter();
        ecranSupprimer = creerEcranSupprimer();
        ecranAugmenter = creerEcranAugmenter();
        ecranComparer = creerEcranComparer();
        ecranAjouter4 = creerEcranAjouter4();

        // Écran par défaut
        getChildren().add(ecranMenu);
    }

    // -----------------------------
    // ÉCRAN MENU PRINCIPAL
    // -----------------------------
    private VBox creerEcranMenu() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));

        Label titre = new Label("MENU PRINCIPAL");

        Button btnAjouter = new Button("Ajouter une moto");
        Button btnListe = new Button("Afficher les motos");
        Button btnSupprimer = new Button("Supprimer une moto");
        Button btnAugmenter = new Button("Augmenter kilométrage");
        Button btnComparer = new Button("Comparer deux motos");
        Button btnAjouter4 = new Button("Ajouter 4 motos prédéfinies");

        btnAjouter.setOnAction(e -> afficherEcran(ecranAjouter));
        btnListe.setOnAction(e -> afficherEcran(ecranListe));
        btnSupprimer.setOnAction(e -> afficherEcran(ecranSupprimer));
        btnAugmenter.setOnAction(e -> afficherEcran(ecranAugmenter));
        btnComparer.setOnAction(e -> afficherEcran(ecranComparer));
        btnAjouter4.setOnAction(e -> afficherEcran(ecranAjouter4));

        box.getChildren().addAll(titre, btnAjouter, btnListe, btnSupprimer, btnAugmenter, btnComparer, btnAjouter4);
        return box;
    }

    // -----------------------------
    // ÉCRAN LISTE DES MOTOS
    // -----------------------------
    private VBox creerEcranListe() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Label titre = new Label("LISTE DES MOTOS");

        ListView<String> list = new ListView<>();
        Button btnRetour = new Button("Retour");

        btnRetour.setOnAction(e -> afficherEcran(ecranMenu));

        // Mise à jour automatique
        box.setOnMouseEntered(e -> {
            list.getItems().clear();
            gestionnaire.getMotos().forEach(m -> list.getItems().add(m.toString()));
        });

        box.getChildren().addAll(titre, list, btnRetour);
        return box;
    }

    // -----------------------------
    // ÉCRAN AJOUTER MOTO
    // -----------------------------
    private VBox creerEcranAjouter() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Label titre = new Label("AJOUTER UNE MOTO");

        ComboBox<Marques> cbMarque = new ComboBox<>();
        cbMarque.getItems().addAll(Marques.values());

        TextField tfModele = new TextField();
        tfModele.setPromptText("Modèle");

        TextField tfAnnee = new TextField();
        tfAnnee.setPromptText("Année");

        TextField tfCouleur = new TextField();
        tfCouleur.setPromptText("Couleur");

        TextField tfKm = new TextField();
        tfKm.setPromptText("Kilométrage");

        Button btnAjouter = new Button("Ajouter");
        Button btnRetour = new Button("Retour");

        btnRetour.setOnAction(e -> afficherEcran(ecranMenu));

        btnAjouter.setOnAction(e -> {
            try {
                Moto m = new Moto(
                        cbMarque.getValue(),
                        tfModele.getText(),
                        Integer.parseInt(tfAnnee.getText()),
                        tfCouleur.getText(),
                        Integer.parseInt(tfKm.getText())
                );
                gestionnaire.ajouterMoto(m);
                new Alert(Alert.AlertType.INFORMATION, "Moto ajoutée !").show();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Erreur dans les données").show();
            }
        });

        box.getChildren().addAll(titre, cbMarque, tfModele, tfAnnee, tfCouleur, tfKm, btnAjouter, btnRetour);
        return box;
    }

    // -----------------------------
    // ÉCRAN SUPPRIMER
    // -----------------------------
    private VBox creerEcranSupprimer() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Label titre = new Label("SUPPRIMER UNE MOTO");

        ListView<String> list = new ListView<>();
        Button btnSupprimer = new Button("Supprimer");
        Button btnRetour = new Button("Retour");

        btnRetour.setOnAction(e -> afficherEcran(ecranMenu));

        btnSupprimer.setOnAction(e -> {
            int index = list.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                gestionnaire.supprimerMoto(index);
                list.getItems().remove(index);
            }
        });

        box.setOnMouseEntered(e -> {
            list.getItems().clear();
            gestionnaire.getMotos().forEach(m -> list.getItems().add(m.toString()));
        });

        box.getChildren().addAll(titre, list, btnSupprimer, btnRetour);
        return box;
    }

    // -----------------------------
    // ÉCRAN AUGMENTER KM
    // -----------------------------
    private VBox creerEcranAugmenter() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Label titre = new Label("AUGMENTER KILOMÉTRAGE");

        ListView<String> list = new ListView<>();
        TextField tfKm = new TextField();
        tfKm.setPromptText("Kilomètres à ajouter");

        Button btnAjouter = new Button("Ajouter");
        Button btnRetour = new Button("Retour");

        btnRetour.setOnAction(e -> afficherEcran(ecranMenu));

        btnAjouter.setOnAction(e -> {
            int index = list.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                gestionnaire.ajouterKilometrage(index, Integer.parseInt(tfKm.getText()));
                list.getItems().set(index, gestionnaire.getMotos().get(index).toString());
            }
        });

        box.setOnMouseEntered(e -> {
            list.getItems().clear();
            gestionnaire.getMotos().forEach(m -> list.getItems().add(m.toString()));
        });

        box.getChildren().addAll(titre, list, tfKm, btnAjouter, btnRetour);
        return box;
    }

    // -----------------------------
    // ÉCRAN COMPARER
    // -----------------------------
    private VBox creerEcranComparer() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Label titre = new Label("COMPARER DEUX MOTOS");

        ListView<String> list = new ListView<>();
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button btnComparer = new Button("Comparer");
        Button btnRetour = new Button("Retour");

        btnRetour.setOnAction(e -> afficherEcran(ecranMenu));

        btnComparer.setOnAction(e -> {
            var selected = list.getSelectionModel().getSelectedIndices();
            if (selected.size() == 2) {
                Moto m1 = gestionnaire.getMotos().get(selected.get(0));
                Moto m2 = gestionnaire.getMotos().get(selected.get(1));

                String msg;
                if (m1.getKilometrage() > m2.getKilometrage())
                    msg = m1 + " a plus roulé.";
                else if (m2.getKilometrage() > m1.getKilometrage())
                    msg = m2 + " a plus roulé.";
                else
                    msg = "Même kilométrage.";

                new Alert(Alert.AlertType.INFORMATION, msg).show();
            }
        });

        box.setOnMouseEntered(e -> {
            list.getItems().clear();
            gestionnaire.getMotos().forEach(m -> list.getItems().add(m.toString()));
        });

        box.getChildren().addAll(titre, list, btnComparer, btnRetour);
        return box;
    }

    // -----------------------------
    // ÉCRAN AJOUTER 4 MOTOS
    // -----------------------------
    private VBox creerEcranAjouter4() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        Label titre = new Label("AJOUTER 4 MOTOS PRÉDÉFINIES");

        Button btn = new Button("Ajouter");
        Button btnRetour = new Button("Retour");

        btnRetour.setOnAction(e -> afficherEcran(ecranMenu));

        btn.setOnAction(e -> {
            gestionnaire.ajouter4Motos();
            new Alert(Alert.AlertType.INFORMATION, "4 motos ajoutées !").show();
        });

        box.getChildren().addAll(titre, btn, btnRetour);
        return box;
    }

    // -----------------------------
    // MÉTHODE POUR CHANGER D'ÉCRAN
    // -----------------------------
    private void afficherEcran(Pane ecran) {
        getChildren().clear();
        getChildren().add(ecran);
    }
}

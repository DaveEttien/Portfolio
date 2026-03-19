package devoir3;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/*
 * Auteurs : Chaiwat Aikaew, Alexandre Boisvert, Emerick Chassé, Dave-Olwyn Eby Ettien, Amadou Tidiane Sall
 * Description :  Un programme permettant de gérer des motos, offrant des fonctionnalités dédiées à la 
 * gestion des marques, modèles, kilométrage, ainsi que des options pour manipuler les motos au sein d'un gestionnaire
 */

public class GestionnaireMotos {

	private List<Moto> motos = new ArrayList<>(); // Liste pour stocker les motos
	private Scanner scanner = new Scanner(System.in);

	public void pilote() {
		int input;
		do {
			afficherChoix();
			input = scanner.nextInt();

			// Traiter le choix de l'utilisateur
			switch (input) {
			case 1:
				creationAjoutMoto();
				break;
			case 2:
				afficherMotoDansList();
				break;
			case 3:
				supprimerMoto();
				break;
			case 4:
				augmenterKilometrage();
				break;
			case 5:
				comparaisonKilometrage();
				break;
			case 6:
				creationEtAjoutQuatreMotos();
				break;
			case 7:
				System.out.println("Option terminée");
				break;
			default:
				System.out.println("Choix invalide,veuillez choisir un chiffre entier entre 1 à 7");
			}
		} while (input != 7); // Continuer jusqu'à ce que l'utilisateur choisisse de quitter
	}

	// Méthode pour afficher les options disponibles
	private void afficherChoix() {
		System.out.println("Veuillez choisir un chiffre pour faire votre choix");
		System.out.println("1 : Création et ajout d’un objet moto à la liste de motos ");
		System.out.println("2 : Affichage de toutes les motos de la liste ");
		System.out.println("3 : Suppression d’une moto ");
		System.out.println("4 : Augmentation du kilométrage d’une moto ");
		System.out.println("5 : Comparaisons du kilométrage de deux motos ");
		System.out.println("6 : Création et ajout de 4 objets motos prédéfinies à la liste des motos ");
		System.out.println("7 : Quitter le menu ");
	}

	// Méthode pour créer et ajouter une moto à la liste
	private void creationAjoutMoto() {

		System.out.println("Veuillez choisir la marque parmi les suivantes");
		System.out.println("Yamaha, Honda, Kawasaki, HarleyDavidson, Typhoon, Suzuki");
		Marques marque = null;
		boolean marqueValide = false;
		scanner.nextLine(); // Pour éviter le problème de lecture d'entrée

		// Boucle pour vérifier que l'utilisateur entre une marque valide
		while (!marqueValide) {
			System.out.println("Entrez la marque");
			String marqueChoisie = scanner.nextLine().trim();
			System.out.println("Vous avez entré : " + marqueChoisie);

			switch (marqueChoisie.toLowerCase()) {
			case "yamaha":
				marque = Marques.Yamaha;
				marqueValide = true;
				break;
			case "honda":
				marque = Marques.Honda;
				marqueValide = true;
				break;
			case "kawasaki":
				marque = Marques.Kawasaki;
				marqueValide = true;
				break;
			case "harleydavidson":
				marque = Marques.HarleyDavidson;
				marqueValide = true;
				break;
			case "typhoon":
				marque = Marques.Typhoon;
				marqueValide = true;
				break;
			case "suzuki":
				marque = Marques.Suzuki;
				marqueValide = true;
				break;
			default:
				System.out.println("Cette marque est invalide, refaites le choix SVP");
				break;
			}
			System.out.println("Marque valide : " + marqueValide);
		}
		// Créer une moto à partir de la marque choisie par l'utilisateur et l'ajouter à
		// la liste
		Moto motoCree = new Moto(marque, "TP1-R3K", 2004, "rouge", 1000);
		motos.add(motoCree);
		System.out.println("Moto créée et ajoutée avec succès !");
	}

	// Méthode pour afficher toutes les motos dans la liste
	private void afficherMotoDansList() {
		if (motos.isEmpty()) {
			System.out.println("Il n'y a pas de moto dans la liste");
		} else {
			System.out.println("Voici la liste de moto");
			for (int i = 0; i < motos.size(); i++) {
				System.out.println((i + 1) + "." + motos.get(i));
			}
		}
	}

	// Méthode pour supprimer une moto de la liste
	private void supprimerMoto() {
		if (motos.isEmpty()) {
			System.out.println("La liste est vide");
			return;
		}
		// Afficher les motos disponibles pour suppression
		for (int i = 0; i < motos.size(); i++) {
			System.out.println((i + 1) + "." + motos.get(i));
		}
		System.out.println("Vous pouvez supprimer une moto en utilisant le numéro");
		int numeroChoisie = scanner.nextInt();
		if (numeroChoisie < 1 || numeroChoisie > motos.size()) {
			System.out.println("Numéro invalide, veuillez réessayer");
			return;
		}
		motos.remove(numeroChoisie - 1);
		System.out.println("Moto numéro " + numeroChoisie + " supprimée avec succès");
	}

	// Méthode pour augmenter le kilométrage d'une moto
	private void augmenterKilometrage() {
		if (motos.isEmpty()) {
			System.out.println("La liste n'a pas de moto");
			return;
		}
		// Afficher les motos disponibles
		for (int i = 0; i < motos.size(); i++) {
			System.out.println((i + 1) + "." + motos.get(i));
		}
		System.out.println("Veuillez choisir la moto que vous voulez augmenter en utilisant le numéro");
		int numeroDeMoto = scanner.nextInt();
		System.out.println("Veuillez mettre le nombre de kilométrage que vous voulez ajouter");
		int kilometrageAjoute = scanner.nextInt();
		Moto moto = motos.get(numeroDeMoto - 1);
		moto.ajouterKilometrage(kilometrageAjoute);
		System.out.println("Kilométrage ajouté avec succès!");
	}

	// Méthode pour comparer le kilométrage de deux motos
	private void comparaisonKilometrage() {
		if (motos.isEmpty()) {
			System.out.println("On ne peut pas faire la comparaison, la liste est vide");
			return;
		} else if (motos.size() == 1) {
			System.out.println("Il y a juste une moto dans la liste, impossible de faire la comparaison");
			return;
		} else if (motos.size() >= 2) {
			// Afficher les motos disponibles
			for (int i = 0; i < motos.size(); i++) {
				System.out.println((i + 1) + "." + motos.get(i));
			}
			System.out.println("Veuillez choisir 2 motos selon le numéro pour comparer le kilométrage ");
			System.out.println("Entrez votre premier choix");
			int numeroPremierMoto = scanner.nextInt();
			System.out.println("Entrez votre deuxième choix");
			int numeroDeuxiemeMoto = scanner.nextInt();
			Moto moto1 = motos.get(numeroPremierMoto - 1);
			Moto moto2 = motos.get(numeroDeuxiemeMoto - 1);
			if (moto1.getKilometrage() < moto2.getKilometrage()) {
				System.out.println("La moto numéro " + numeroDeuxiemeMoto + " a parcouru "
						+ (moto2.getKilometrage() - moto1.getKilometrage()) + " kilomètres plus que l'autre moto");
			} else if (moto1.getKilometrage() > moto2.getKilometrage()) {
				System.out.println("La moto numéro " + numeroPremierMoto + " a parcouru "
						+ (moto1.getKilometrage() - moto2.getKilometrage()) + " kilomètres plus que l'autre moto");
			} else {
				System.out.println("Les deux motos ont le même nombre de kilométrage");
			}
		}
	}

	// Méthode pour ajouter 4 motos prédéfinies à la liste
	private void creationEtAjoutQuatreMotos() {
		Moto motoA = new Moto(Marques.Honda, "RX3-YZZ", 1996, "rouge", 10000);
		Moto motoB = new Moto(Marques.HarleyDavidson, "OKI-12T", 1999, "noir", 15000);
		Moto motoC = new Moto(Marques.Kawasaki, "NINJA", 2010, "bleu", 5000);
		Moto motoD = new Moto(Marques.Yamaha, "SWA-19X", 2002, "gris", 23000);
		motos.add(motoA);
		motos.add(motoB);
		motos.add(motoC);
		motos.add(motoD);
		System.out.println("4 motos ajoutées avec succès!");
	}

	// Méthode principale pour démarrer le programme
	public static void main(String[] args) {
		GestionnaireMotos gestionnaire = new GestionnaireMotos();
		gestionnaire.pilote();
	}
}

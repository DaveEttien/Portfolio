package devoir3;

public class Moto {

	// Déclaration des variables
	private Marques marque;
	private String modele;
	private int annee;
	private String couleur;
	private int kilometrage;

	// Constructeur de la classe
	public Moto(Marques marque, String modele, int annee, String couleur, int kilometrage) {
		this.marque = marque;
		this.modele = modele;
		this.annee = annee;
		this.couleur = couleur;
		this.kilometrage = kilometrage;
	}

	// Accesseurs pour les variables d'instance
	public Marques getMarque() {
		return marque;
	}

	public String getModele() {
		return modele;
	}

	public int getAnnee() {
		return annee;
	}

	public String getCouleur() {
		return couleur;
	}

	public int getKilometrage() {
		return kilometrage;
	}

	// Mutateurs pour les variables d'instances
	public void setMarque(Marques marque) {
		this.marque = marque;
	}

	public void setModele(String modele) {
		this.modele = modele;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}

	public void setKilometrage(int kilometrage) {
		this.kilometrage = kilometrage;
	}

	public void ajouterKilometrage(int kilometrageAjoutee) {
		this.kilometrage += kilometrageAjoutee;
	}

	public String toString() {
		return "Marque : " + marque + "| Modèle : " + modele + "| Année : " + annee + "| Couleur : " + couleur
				+ "| kilometrage : " + kilometrage;
	}
}

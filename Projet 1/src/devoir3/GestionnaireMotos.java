package devoir3;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireMotos {

    private List<Moto> motos = new ArrayList<>();

    public List<Moto> getMotos() {
        return motos;
    }

    public void ajouterMoto(Moto moto) {
        motos.add(moto);
    }

    public void supprimerMoto(int index) {
        motos.remove(index);
    }

    public void ajouterKilometrage(int index, int km) {
        motos.get(index).ajouterKilometrage(km);
    }

    public void ajouter4Motos() {
        motos.add(new Moto(Marques.Honda, "RX3-YZZ", 1996, "rouge", 10000));
        motos.add(new Moto(Marques.HarleyDavidson, "OKI-12T", 1999, "noir", 15000));
        motos.add(new Moto(Marques.Kawasaki, "NINJA", 2010, "bleu", 5000));
        motos.add(new Moto(Marques.Yamaha, "SWA-19X", 2002, "gris", 23000));
    }
}

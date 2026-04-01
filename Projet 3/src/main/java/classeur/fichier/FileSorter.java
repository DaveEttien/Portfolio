package classeur.fichier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileSorter {

    public static void sort(Path file) throws IOException {

        String fileName = file.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");

        // Aucun point → pas d’extension → on ignore
        if (dotIndex == -1) return;

        // Extraire l’extension (.pdf, .jpg, etc.)
        String extension = fileName.substring(dotIndex);

        // Trouver le dossier correspondant dans Config
        String folder = Config.getDestinationFolder(extension);

        // Si aucune règle pour cette extension → on ignore
        if (folder != null) {

            // Construire le chemin du dossier de destination
            Path destination = file.getParent().resolve(folder);

            // Créer le dossier si nécessaire
            Files.createDirectories(destination);

            // Déplacer le fichier
            Files.move(
                file,
                destination.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("Fichier déplacé vers : " + folder);
        }
    }
}
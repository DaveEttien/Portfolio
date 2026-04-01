package classeur.fichier;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final Map<String, String> RULES = new HashMap<>();

    static {
        // Documents
        RULES.put(".pdf", "Documents");
        RULES.put(".doc", "Documents");
        RULES.put(".docx", "Documents");
        RULES.put(".txt", "Documents");

        // Images
        RULES.put(".jpg", "Images");
        RULES.put(".jpeg", "Images");
        RULES.put(".png", "Images");
        RULES.put(".gif", "Images");

        // Audio
        RULES.put(".mp3", "Music");
        RULES.put(".wav", "Music");

        // Vidéos
        RULES.put(".mp4", "Videos");
        RULES.put(".mkv", "Videos");

        // Archives
        RULES.put(".zip", "Archives");
        RULES.put(".rar", "Archives");
        RULES.put(".7z", "Archives");

        // Programmes
        RULES.put(".exe", "Programs");
        RULES.put(".msi", "Programs");
    }

    public static String getDestinationFolder(String extension) {
        return RULES.get(extension.toLowerCase());
    }
}
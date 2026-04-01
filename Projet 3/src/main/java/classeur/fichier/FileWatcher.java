package classeur.fichier;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.ListView;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcher {

    private final Path path;
    private final ListView<String> logList;
    private WatchService watchService;
    private boolean running = false;

    private int existingCount = 0;
    private int newCount = 0;

    public FileWatcher(Path path, ListView<String> logList) {
        this.path = path;
        this.logList = logList;
    }

    // Tri des fichiers déjà présents
    private void sortExistingFiles() {
    existingCount = 0;

    try {
        for (Path file : Files.list(path)
                .filter(Files::isRegularFile)
                .collect(java.util.stream.Collectors.toList())) {

            try {
                FileSorter.sort(file);
                existingCount++;

                Platform.runLater(() ->
                    logList.getItems().add("Fichier existant trié : " + file.getFileName())
                );

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int finalCount = existingCount;
        Platform.runLater(() ->
            logList.getItems().add(finalCount + " fichier(s) existant(s) trié(s).")
        );

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    // Démarrage de la surveillance
    public void start() {
        running = true;

        // Tri initial
        sortExistingFiles();

        Thread thread = new Thread(() -> {
            try {
                watchService = FileSystems.getDefault().newWatchService();
                path.register(watchService, ENTRY_CREATE);

                Platform.runLater(() ->
                    logList.getItems().add("Surveillance du dossier : " + path)
                );

                while (running) {
                    WatchKey key;

                    try {
                        key = watchService.take();
                    } catch (InterruptedException | ClosedWatchServiceException e) {
                        break;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == ENTRY_CREATE) {
                            Path fileName = (Path) event.context();
                            Path fullPath = path.resolve(fileName);

                            Platform.runLater(() ->
                                logList.getItems().add("Nouveau fichier détecté : " + fileName)
                            );

                            FileSorter.sort(fullPath);
                            newCount++;

                            int finalNewCount = newCount;
                            Platform.runLater(() ->
                                logList.getItems().add("Total nouveaux fichiers triés : " + finalNewCount)
                            );
                        }
                    }

                    key.reset();
                }

            } catch (IOException e) {
                Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, null, e);
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    // Arrêt propre
    public void stopWatching() {
        running = false;

        try {
            if (watchService != null) {
                watchService.close();
            }
        } catch (IOException | ClosedWatchServiceException e) {
            Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
package fr.afpa.javacard.utils;

import fr.afpa.javacard.models.Contact;
import java.io.File;
import java.util.List;

public interface ExportService {
    /**
     * Exporte la liste de contacts dans le fichier destination.
     * @param contacts liste de contacts à exporter
     * @param file fichier de destination
     * @param callback pour gérer la progression (optionnel)
     * @return true si export réussi, false sinon
     */
    boolean exporter(List<Contact> contacts, File file, ProgressCallback callback);

    String getExtensionFichier();
    String getDescriptionFormat();
}


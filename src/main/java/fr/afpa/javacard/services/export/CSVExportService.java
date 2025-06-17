package fr.afpa.javacard.services.export;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.utils.ExportService;
import fr.afpa.javacard.utils.ProgressCallback;

import java.io.*;
import java.util.List;

public class CSVExportService implements ExportService {
    @Override
    public boolean exporter(List<Contact> contacts, File file, ProgressCallback callback) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            // En-tête CSV
            writer.println("nom,prenom,email,telephone,adresse,lienCode");

            int total = contacts.size();
            for (int i = 0; i < total; i++) {
                Contact c = contacts.get(i);
                writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        echapper(c.getNom()),
                        echapper(c.getPrenom()),
                        echapper(c.getEmail()),
                        echapper(c.getTelPerso()),    // <-- PRO
                        echapper(c.getTelPro()),      // <-- PRO
                        echapper(c.getAdresse()),
                        echapper(c.getLienCode())     // <-- PRO
                );
                if (callback != null) callback.updateProgress(5 + (90.0 * (i + 1) / total));
            }
            if (callback != null) callback.updateProgress(100);
            return true;
        } catch (Exception e) { System.err.println("Erreur export CSV: " + e.getMessage()); return false; }
    }

    private String echapper(String texte) {
        return (texte == null) ? "" : texte.replace("\"", "\"\"");
    }

    @Override public String getExtensionFichier() { return ".csv"; }
    @Override public String getDescriptionFormat() { return "Fichier CSV (*.csv)"; }
}


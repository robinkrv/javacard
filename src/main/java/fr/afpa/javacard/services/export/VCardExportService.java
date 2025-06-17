package fr.afpa.javacard.services.export;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.utils.ExportService;
import fr.afpa.javacard.utils.ProgressCallback;

import java.io.*;
import java.util.List;

public class VCardExportService implements ExportService {
    @Override
    public boolean exporter(List<Contact> contacts, File file, ProgressCallback callback) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            int total = contacts.size();
            for (int i = 0; i < total; i++) {
                Contact c = contacts.get(i);

                writer.println("BEGIN:VCARD");
                writer.println("VERSION:3.0");
                writer.println("FN:" + c.getPrenom() + " " + c.getNom());
                writer.println("N:" + c.getNom() + ";" + c.getPrenom() + ";;;");
                if (!c.getEmail().isEmpty()) writer.println("EMAIL:" + c.getEmail());
                if (c.getTelPerso() != null && !c.getTelPerso().isEmpty())
                    writer.println("TEL;TYPE=HOME:" + c.getTelPerso());
                if (c.getTelPro() != null && !c.getTelPro().isEmpty())
                    writer.println("TEL;TYPE=WORK:" + c.getTelPro());
                if (!c.getAdresse().isEmpty()) writer.println("ADR:;;" + c.getAdresse() + ";;;;");
                if (c.getLienCode() != null && !c.getLienCode().isEmpty())
                    writer.println("URL:" + c.getLienCode());
                writer.println("END:VCARD");
                if (i < total - 1) writer.println(); // ligne vide entre chaque contact

                if (callback != null) callback.updateProgress(5 + (90.0 * (i + 1) / total));
            }
            if (callback != null) callback.updateProgress(100);
            return true;
        } catch (Exception e) { System.err.println("Erreur export VCard: " + e.getMessage()); return false; }
    }

    @Override public String getExtensionFichier() { return ".vcf"; }
    @Override public String getDescriptionFormat() { return "Carte de visite (*.vcf)"; }
}

package fr.afpa.javacard.services.export;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.utils.ExportService;
import fr.afpa.javacard.utils.ProgressCallback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.List;

public class JSONExportService implements ExportService {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public boolean exporter(List<Contact> contacts, File file, ProgressCallback callback) {
        try {
            mapper.writeValue(file, contacts);
            if (callback != null) callback.updateProgress(100);
            return true;
        } catch (Exception e) {
            System.err.println("Erreur export JSON (Jackson) : " + e.getMessage());
            return false;
        }
    }

    @Override public String getExtensionFichier() { return ".json"; }
    @Override public String getDescriptionFormat() { return "Fichier JSON (*.json)"; }
}

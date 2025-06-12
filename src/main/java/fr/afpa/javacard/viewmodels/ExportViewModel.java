package fr.afpa.javacard.viewmodels;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.services.ContactService;
import fr.afpa.javacard.utils.ExportService;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import java.io.File;
import java.util.List;

public class ExportViewModel {

    private final ContactService contactService;
    private final ExportService exportService;

    private final BooleanProperty exportEnCours = new SimpleBooleanProperty(false);
    private final DoubleProperty progressionExport = new SimpleDoubleProperty(0.0);
    private final StringProperty messageStatut = new SimpleStringProperty("");
    private final ObjectProperty<File> fichierDestination = new SimpleObjectProperty<>();

    private final StringProperty formatSelectionne = new SimpleStringProperty("CSV");

    public ExportViewModel(ContactService contactService, ExportService exportService) {
        this.contactService = contactService;
        this.exportService = exportService;
    }

    public void exporterTousLesContacts(File destination) {
        if (destination == null) return;

        fichierDestination.set(destination);
        List<Contact> contacts = contactService.getContacts();
        lancerExport(contacts, destination);
    }

    public void exporterContactsSelectionnes(List<Contact> contactsSelectionnes, File destination) {
        if (contactsSelectionnes == null || contactsSelectionnes.isEmpty() || destination == null) {
            messageStatut.set("❌ Aucun contact sélectionné ou destination invalide");
            return;
        }

        fichierDestination.set(destination);
        lancerExport(contactsSelectionnes, destination);
    }

    private void lancerExport(List<Contact> contacts, File destination) {
        Task<Boolean> tacheExport = creerTacheExport(contacts, destination);

        progressionExport.bind(tacheExport.progressProperty());
        exportEnCours.bind(tacheExport.runningProperty());


        tacheExport.setOnSucceeded(e -> {
            Boolean success = tacheExport.getValue();
            if (success) {
                messageStatut.set("✅ Export terminé avec succès !");
            } else {
                messageStatut.set("❌ Erreur lors de l'export");
            }
        });

        tacheExport.setOnFailed(e -> {
            Throwable exception = tacheExport.getException();
            messageStatut.set("❌ Erreur : " + exception.getMessage());
        });

        Thread threadExport = new Thread(tacheExport);
        threadExport.setDaemon(true);
        threadExport.start();
    }

    private Task<Boolean> creerTacheExport(List<Contact> contacts, File destination) {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                updateMessage("📤 Préparation de l'export...");
                updateProgress(0, 100);

                String format = formatSelectionne.get();
                int totalContacts = contacts.size();

                try {
                    switch (format.toUpperCase()) {
                        case "CSV":
                            return exportService.exporter(contacts, destination,
                                    (progression) -> {
                                        updateProgress(progression, 100);
                                        updateMessage("📤 Export CSV... " + (int)progression + "%");
                                    });

                        case "JSON":
                            return exportService.exporter(contacts, destination,
                                    (progression) -> {
                                        updateProgress(progression, 100);
                                        updateMessage("📤 Export JSON... " + (int)progression + "%");
                                    });

                        case "VCard:":
                            return exportService.exporter(contacts, destination, (progression) -> {
                                updateProgress(progression, 100);
                                updateMessage("📤 Export VCard... " + (int)progression + "%");
                            });

                        default:
                            throw new UnsupportedOperationException("Format non supporté: " + format);
                    }
                } catch (Exception e) {
                    updateMessage("❌ Erreur: " + e.getMessage());
                    throw e;
                }
            }
        };
    }
    public void annulerExport() {
        messageStatut.set("🛑 Export annulé");
    }

    public BooleanProperty exportEnCoursProperty() { return exportEnCours; }
    public DoubleProperty progressionExportProperty() { return progressionExport; }
    public StringProperty messageStatutProperty() { return messageStatut; }
    public ObjectProperty<File> fichierDestinationProperty() { return fichierDestination; }
    public StringProperty formatSelectionneProperty() { return formatSelectionne; }

    public boolean isExportEnCours() { return exportEnCours.get(); }
    public double getProgressionExport() { return progressionExport.get(); }
    public String getMessageStatut() { return messageStatut.get(); }
    public File getFichierDestination() { return fichierDestination.get(); }
    public String getFormatSelectionne() { return formatSelectionne.get(); }

    public void setFormatSelectionne(String format) { this.formatSelectionne.set(format); }
}


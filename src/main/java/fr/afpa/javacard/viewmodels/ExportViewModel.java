package fr.afpa.javacard.viewmodels;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.services.ContactService;
import fr.afpa.javacard.utils.ExportService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExportViewModel {
    private final ContactService contactService;
    private final ExportService exportService;

    private final ObservableList<Contact> contactsDisponibles;
    private final ObservableList<Contact> contactsSelectionnes = FXCollections.observableArrayList();

    private final BooleanProperty exportEnCours = new SimpleBooleanProperty(false);
    private final DoubleProperty progressionExport = new SimpleDoubleProperty(0.0);
    private final StringProperty messageStatut = new SimpleStringProperty("");
    private final ObjectProperty<File> fichierDestination = new SimpleObjectProperty<>();
    private final StringProperty formatSelectionne = new SimpleStringProperty("CSV");

    public ExportViewModel(ContactService contactService, ExportService exportService) {
        this.contactService = contactService;
        this.exportService = exportService;
        this.contactsDisponibles = FXCollections.observableArrayList(contactService.getContacts());
    }

    // Ajout d'un contact à la sélection
    public void ajouterContact(Contact c) {
        if (c != null && !contactsSelectionnes.contains(c)) {
            contactsSelectionnes.add(c);
        }
    }

    public void retirerContact(Contact c) {
        if (c != null) contactsSelectionnes.remove(c);
    }

    public ObservableList<Contact> getContactsDisponibles() { return contactsDisponibles; }
    public ObservableList<Contact> getContactsSelectionnes() { return contactsSelectionnes; }

    // EXPORT
    public void exporterContactsSelectionnes(File destination) {
        List<Contact> contacts = new ArrayList<>(contactsSelectionnes);
        if (contacts.isEmpty()) {
            messageStatut.set("❌ Aucun contact sélectionné !");
            return;
        }
        fichierDestination.set(destination);
        lancerExport(contacts, destination);
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
                try {
                    return exportService.exporter(contacts, destination, (progression) -> {
                        updateProgress(progression, 100);
                        updateMessage("📤 Export... " + (int) progression + "%");
                    });
                } catch (Exception e) {
                    updateMessage("❌ Erreur: " + e.getMessage());
                    throw e;
                }
            }
        };
    }

    // Getters et Bindings pour UI
    public BooleanProperty exportEnCoursProperty() { return exportEnCours; }
    public DoubleProperty progressionExportProperty() { return progressionExport; }
    public StringProperty messageStatutProperty() { return messageStatut; }
    public ObjectProperty<File> fichierDestinationProperty() { return fichierDestination; }
    public StringProperty formatSelectionneProperty() { return formatSelectionne; }

    public void setFormatSelectionne(String format) { this.formatSelectionne.set(format); }
    public String getMessageStatut() { return messageStatut.get(); }
}

package fr.afpa.javacard.services.persistence;

import fr.afpa.javacard.models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;

public class DataPersistenceService {

    private static final String DOSSIER_DONNEES = getDataDirectory();
    private static final String FICHIER_SAUVEGARDE = "contacts.dat";
    private static final String CHEMIN_COMPLET = DOSSIER_DONNEES + File.separator + FICHIER_SAUVEGARDE;


    public static void sauvegarderContacts(ObservableList<Contact> contacts) {
        try {
            creerDossierSiNecessaire();

            Properties props = new Properties();

            props.setProperty("meta.version", "1.0");
            props.setProperty("meta.date_sauvegarde", LocalDateTime.now().toString());
            props.setProperty("meta.nombre_contacts", String.valueOf(contacts.size()));

            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                String prefix = "contact." + i + ".";

                props.setProperty(prefix + "nom",
                        contact.nomProperty().get() != null ? contact.nomProperty().get() : "");
                props.setProperty(prefix + "prenom",
                        contact.prenomProperty().get() != null ? contact.prenomProperty().get() : "");
                props.setProperty(prefix + "email",
                        contact.emailProperty().get() != null ? contact.emailProperty().get() : "");
                props.setProperty(prefix + "telephone",
                        contact.telephoneProperty().get() != null ? contact.telephoneProperty().get() : "");
                props.setProperty(prefix + "adresse",
                        contact.adresseProperty().get() != null ? contact.adresseProperty().get() : "");
                props.setProperty(prefix + "github",
                        contact.lienGitHubProperty().get() != null ? contact.lienGitHubProperty().get() : "");
                props.setProperty(prefix + "gitlab",
                        contact.lienGitLabProperty().get() != null ? contact.lienGitLabProperty().get() : "");
                props.setProperty(prefix + "date_creation",
                        contact.dateCreationProperty().get() != null ? contact.dateCreationProperty().get().toString() : "");
                props.setProperty(prefix + "date_modification",
                        contact.dateModificationProperty().get() != null ? contact.dateModificationProperty().get().toString() : "");
            }

            try (FileOutputStream fos = new FileOutputStream(CHEMIN_COMPLET)) {
                props.store(fos, "Sauvegarde des contacts - " + LocalDateTime.now());
            }

            System.out.println("✅ Contacts sauvegardés: " + contacts.size() + " contacts");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la sauvegarde: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ObservableList<Contact> chargerContacts() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        try {
            if (!fichierExiste()) {
                System.out.println("ℹ️ Aucun fichier de sauvegarde trouvé, démarrage avec liste vide");
                return contacts;
            }

            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(CHEMIN_COMPLET)) {
                props.load(fis);
            }

            String version = props.getProperty("meta.version", "unknown");
            String dateSauvegarde = props.getProperty("meta.date_sauvegarde", "unknown");
            int nombreContacts = Integer.parseInt(props.getProperty("meta.nombre_contacts", "0"));

            System.out.println("📂 Chargement des données (version " + version +
                    ", " + nombreContacts + " contacts)");

            for (int i = 0; i < nombreContacts; i++) {
                String prefix = "contact." + i + ".";

                Contact contact = new Contact();

                contact.nomProperty().set(props.getProperty(prefix + "nom", ""));
                contact.prenomProperty().set(props.getProperty(prefix + "prenom", ""));
                contact.emailProperty().set(props.getProperty(prefix + "email", ""));
                contact.telephoneProperty().set(props.getProperty(prefix + "telephone", ""));
                contact.adresseProperty().set(props.getProperty(prefix + "adresse", ""));
                contact.lienGitHubProperty().set(props.getProperty(prefix + "github", ""));
                contact.lienGitLabProperty().set(props.getProperty(prefix + "gitlab", ""));

                String dateCreation = props.getProperty(prefix + "date_creation", "");
                if (!dateCreation.isEmpty()) {
                    contact.dateCreationProperty().set(LocalDateTime.parse(dateCreation));
                }

                String dateModification = props.getProperty(prefix + "date_modification", "");
                if (!dateModification.isEmpty()) {
                    contact.dateModificationProperty().set(LocalDateTime.parse(dateModification));
                }

                contacts.add(contact);
            }

            System.out.println("✅ Contacts chargés: " + contacts.size() + " contacts");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du chargement: " + e.getMessage());
            e.printStackTrace();
        }

        return contacts;
    }

    public static boolean fichierExiste() {
        return Files.exists(Paths.get(CHEMIN_COMPLET));
    }

    // 📁 Créer le dossier de sauvegarde
    private static void creerDossierSiNecessaire() throws IOException {
        Path dossier = Paths.get(DOSSIER_DONNEES);
        if (!Files.exists(dossier)) {
            Files.createDirectories(dossier);
            System.out.println("📁 Dossier de données créé: " + DOSSIER_DONNEES);
        }
    }

    private static String getDataDirectory() {
        return System.getProperty("javacard.datadir", "C:\\\\LiberKey\\\\MyApps\\\\laragon\\\\www\\\\javacard\\\\data");
    }
}

package fr.afpa.javacard.services;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.services.persistence.DataPersistenceService;
import fr.afpa.javacard.services.validation.ContactValidator;
import fr.afpa.javacard.services.validation.ValidationResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ContactService {

    private final ObservableList<Contact> contacts;
    private final DataPersistenceService persistence = new DataPersistenceService();

    public ContactService() {
        // 1. Charge si existe, sinon contacts de démo
        List<Contact> charges = persistence.chargerContacts();
        if (charges != null) {
            contacts = FXCollections.observableArrayList(charges);
        } else {
            contacts = FXCollections.observableArrayList();

            // Contacts de test (init seulement si fichier absent !)
            Contact c1 = new Contact();
            c1.setNom("Dupont");
            c1.setPrenom("Alice");
            c1.setGenre("Féminin");
            c1.setDateNaissance(LocalDate.of(1990, 5, 14));
            c1.setPseudonyme("Alicia");
            c1.setAdresse("12 rue des Lilas, 75012 Paris");
            c1.setTelPerso("0611223344");
            c1.setTelPro("0144001122");
            c1.setEmail("alice.dupont@email.com");

            Contact c2 = new Contact();
            c2.setNom("Martin");
            c2.setPrenom("Pierre");
            c2.setGenre("Masculin");
            c2.setDateNaissance(LocalDate.of(1985, 2, 28));
            c2.setPseudonyme("Pierrot");
            c2.setAdresse("8 avenue Victor Hugo, 69002 Lyon");
            c2.setTelPerso("0622334455");
            c2.setTelPro("0457402301");
            c2.setEmail("pierre.martin@email.com");

            contacts.addAll(c1, c2);

            // Sauvegarde immédiate de la démo pour ne plus les perdre à la fermeture !
            persistence.sauvegarderContacts(contacts);
        }
    }


    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    public boolean ajouterContact(Contact contact) {
        try {
            ValidationResult validation = ContactValidator.validateContact(contact);
            if (!validation.isValide()) {
                throw new IllegalArgumentException("Contact invalide:\n" + validation.getMessageErreur());
            }

            if (contactExiste(contact.nomProperty().get(), contact.prenomProperty().get())) {
                throw new IllegalArgumentException("Un contact avec ce nom et prénom existe déjà");
            }

            contacts.add(contact);

            // ✨ Sauvegarde à chaque ajout
            persistence.sauvegarderContacts(contacts);

            System.out.println("✅ Contact ajouté: " + contact.nomProperty().get() + " " + contact.prenomProperty().get());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'ajout: " + e.getMessage());
            return false;
        }
    }

    // ✏️ Modifier un contact
    public boolean modifierContact(Contact ancien, Contact nouveau) {
        int idx = contacts.indexOf(ancien);
        if (idx != -1) {
            contacts.set(idx, nouveau);
            persistence.sauvegarderContacts(contacts);
            System.out.println("✅ Contact modifié !");
            return true;
        } else {
            System.err.println("❌ Contact à modifier non trouvé !");
            return false;
        }
    }

    public boolean supprimerContact(Contact contact) {
        try {
            boolean supprime = contacts.remove(contact);

            if (supprime) {
                // ✨ Sauvegarde à chaque suppression
                persistence.sauvegarderContacts(contacts);

                System.out.println("✅ Contact supprimé: " + contact.nomProperty().get());
            } else {
                System.out.println("⚠️ Contact non trouvé pour suppression");
            }
            return supprime;

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }

    // 🔍 Rechercher un contact par nom et prénom
    public Contact rechercherContact(String nom, String prenom) {
        return contacts.stream()
                .filter(c -> c.nomProperty().get().equalsIgnoreCase(nom) &&
                        c.prenomProperty().get().equalsIgnoreCase(prenom))
                .findFirst()
                .orElse(null);
    }

    public ObservableList<Contact> rechercherParCritere(String critere) {
        if (critere == null || critere.trim().isEmpty()) {
            return FXCollections.observableArrayList(contacts);
        }

        String critereMin = critere.toLowerCase().trim();

        return contacts.stream()
                .filter(contact ->
                        contact.nomProperty().get().toLowerCase().contains(critereMin) ||
                                contact.prenomProperty().get().toLowerCase().contains(critereMin) ||
                                (contact.emailProperty().get() != null &&
                                        contact.emailProperty().get().toLowerCase().contains(critereMin))
                )
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public int getNombreContacts() {
        return contacts.size();
    }

    // 🔍 Vérifier si un contact existe
    private boolean contactExiste(String nom, String prenom) {
        return contacts.stream()
                .anyMatch(c -> c.nomProperty().get().equalsIgnoreCase(nom) &&
                        c.prenomProperty().get().equalsIgnoreCase(prenom));
    }

    // 🧹 Nettoyer tous les contacts
    public void viderContacts() {
        contacts.clear();
        persistence.sauvegarderContacts(contacts);
        System.out.println("🧹 Tous les contacts ont été supprimés");
    }

    // 📋 Obtenir une copie des contacts (pour sécurité)
    public ObservableList<Contact> getContactsCopie() {
        return FXCollections.observableArrayList(contacts);
    }
}

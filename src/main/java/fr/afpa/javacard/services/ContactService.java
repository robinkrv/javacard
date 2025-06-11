package fr.afpa.javacard.services;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.services.validation.ContactValidator;
import fr.afpa.javacard.services.validation.ValidationResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class ContactService {

    private final ObservableList<Contact> contacts;

    public ContactService() {
        this.contacts = FXCollections.observableArrayList();
    }

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    // ➕ Ajouter un contact
    public boolean ajouterContact(Contact contact) {
        try {
            // 🛡️ Validation
            ValidationResult validation = ContactValidator.validateContact(contact);
            if (!validation.isValide()) {
                throw new IllegalArgumentException(
                        "Contact invalide:\n" + validation.getMessageErreur()
                );
            }

            // 🔍 Vérification unicité (nom + prénom)
            if (contactExiste(contact.nomProperty().get(), contact.prenomProperty().get())) {
                throw new IllegalArgumentException(
                        "Un contact avec ce nom et prénom existe déjà"
                );
            }

            // 📅 Définir la date de création
            contact.dateCreationProperty().set(LocalDateTime.now());
            contact.dateModificationProperty().set(LocalDateTime.now());

            // ✅ Ajouter à la liste
            contacts.add(contact);

            System.out.println("✅ Contact ajouté: " + contact.nomProperty().get() +
                    " " + contact.prenomProperty().get());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'ajout: " + e.getMessage());
            return false;
        }
    }

    // ✏️ Modifier un contact
    public boolean modifierContact(Contact contact) {
        try {
            // 🛡️ Validation
            ValidationResult validation = ContactValidator.validateContact(contact);
            if (!validation.isValide()) {
                throw new IllegalArgumentException(
                        "Contact invalide:\n" + validation.getMessageErreur()
                );
            }

            // 🔍 Trouver le contact existant
            Contact existant = rechercherContact(
                    contact.nomProperty().get(),
                    contact.prenomProperty().get()
            );

            if (existant == null) {
                throw new IllegalArgumentException("Contact introuvable");
            }

            // 📅 Update date de modification
            contact.dateModificationProperty().set(LocalDateTime.now());

            // 🔄 Remplacer dans la liste
            int index = contacts.indexOf(existant);
            contacts.set(index, contact);

            System.out.println("✅ Contact modifié: " + contact.nomProperty().get());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la modification: " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerContact(Contact contact) {
        try {
            boolean supprime = contacts.remove(contact);
            if (supprime) {
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

    // 🔎 Rechercher par critère (nom, prénom, email)
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
        System.out.println("🧹 Tous les contacts ont été supprimés");
    }

    // 📋 Obtenir une copie des contacts (pour sécurité)
    public ObservableList<Contact> getContactsCopie() {
        return FXCollections.observableArrayList(contacts);
    }
}


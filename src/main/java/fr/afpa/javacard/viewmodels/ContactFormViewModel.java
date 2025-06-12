package fr.afpa.javacard.viewmodels;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.services.validation.ContactValidator;
import fr.afpa.javacard.services.validation.ValidationResult;
import javafx.beans.property.*;

public class ContactFormViewModel {

    private final StringProperty nom = new SimpleStringProperty("");
    private final StringProperty prenom = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty telephone = new SimpleStringProperty("");
    private final StringProperty adresse = new SimpleStringProperty("");
    private final StringProperty lienGitHub = new SimpleStringProperty("");
    private final StringProperty lienGitLab = new SimpleStringProperty("");

    private final BooleanProperty formulaireValide = new SimpleBooleanProperty(false);
    private final StringProperty messageErreur = new SimpleStringProperty("");

    private Contact contactOriginal;

    public ContactFormViewModel() {
        configurerValidation();
    }

    public void chargerContact(Contact contact) {
        this.contactOriginal = contact;

        if (contact != null) {
            nom.set(contact.nomProperty().get());
            prenom.set(contact.prenomProperty().get());
            email.set(contact.emailProperty().get());
            telephone.set(contact.telephoneProperty().get());
            adresse.set(contact.adresseProperty().get());
            lienGitHub.set(contact.lienGitHubProperty().get());
            lienGitLab.set(contact.lienGitLabProperty().get());
        } else {
            viderFormulaire();
        }
    }

    public Contact sauvegarder() {
        Contact contact = (contactOriginal != null) ? contactOriginal : new Contact();

        contact.nomProperty().set(nom.get());
        contact.prenomProperty().set(prenom.get());
        contact.emailProperty().set(email.get());
        contact.telephoneProperty().set(telephone.get());
        contact.adresseProperty().set(adresse.get());
        contact.lienGitHubProperty().set(lienGitHub.get());
        contact.lienGitLabProperty().set(lienGitLab.get());

        return contact;
    }

    private void configurerValidation() {
        nom.addListener((obs, old, val) -> valider());
        prenom.addListener((obs, old, val) -> valider());
        email.addListener((obs, old, val) -> valider());
    }

    private void valider() {
        Contact contactTemp = new Contact();
        contactTemp.nomProperty().set(nom.get());
        contactTemp.prenomProperty().set(prenom.get());
        contactTemp.emailProperty().set(email.get());
        contactTemp.telephoneProperty().set(telephone.get());
        contactTemp.lienGitHubProperty().set(lienGitHub.get());
        contactTemp.lienGitLabProperty().set(lienGitLab.get());

        ValidationResult result = ContactValidator.validateContact(contactTemp);
        formulaireValide.set(result.isValide());
        messageErreur.set(result.getMessageErreur());
    }

    private void viderFormulaire() {
        nom.set("");
        prenom.set("");
        email.set("");
        telephone.set("");
        adresse.set("");
        lienGitHub.set("");
        lienGitLab.set("");
    }

    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty emailProperty() { return email; }
    public StringProperty telephoneProperty() { return telephone; }
    public StringProperty adresseProperty() { return adresse; }
    public StringProperty lienGitHubProperty() { return lienGitHub; }
    public StringProperty lienGitLabProperty() { return lienGitLab; }
    public BooleanProperty formulaireValideProperty() { return formulaireValide; }
    public StringProperty messageErreurProperty() { return messageErreur; }
}


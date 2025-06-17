package fr.afpa.javacard.viewmodels;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.services.validation.ContactValidator;
import fr.afpa.javacard.services.validation.ValidationResult;
import javafx.beans.property.*;

import java.time.LocalDate;

public class ContactFormViewModel {

    private final StringProperty nom = new SimpleStringProperty("");
    private final StringProperty prenom = new SimpleStringProperty("");
    private final StringProperty genre = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dateNaissance = new SimpleObjectProperty<>();
    private final StringProperty pseudonyme = new SimpleStringProperty("");
    private final StringProperty adresse = new SimpleStringProperty("");
    private final StringProperty telPerso = new SimpleStringProperty("");
    private final StringProperty telPro = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty lienCode = new SimpleStringProperty(""); // Lien GitHub/GitLab

    private final BooleanProperty formulaireValide = new SimpleBooleanProperty(false);
    private final StringProperty messageErreur = new SimpleStringProperty("");

    private final BooleanProperty formulaireSoumis = new SimpleBooleanProperty(false);

    public BooleanProperty formulaireSoumisProperty() { return formulaireSoumis; }
    public boolean isFormulaireSoumis() { return formulaireSoumis.get(); }
    public void setSoumis(boolean value) { formulaireSoumis.set(value); }

    private Contact contactOriginal;

    public ContactFormViewModel() {
        configurerValidation();
    }

    public void chargerContact(Contact contact) {
        this.contactOriginal = contact;

        if (contact != null) {
            nom.set(contact.getNom());
            prenom.set(contact.getPrenom());
            genre.set(contact.getGenre());
            dateNaissance.set(contact.getDateNaissance());
            pseudonyme.set(contact.getPseudonyme());
            adresse.set(contact.getAdresse());
            telPerso.set(contact.getTelPerso());
            telPro.set(contact.getTelPro());
            email.set(contact.getEmail());
            lienCode.set(contact.getLienCode());
        } else {
            viderFormulaire();
        }
        valider();
    }


    public Contact sauvegarder() {
        Contact contact = (contactOriginal != null) ? contactOriginal : new Contact();
        contact.setNom(nom.get());
        contact.setPrenom(prenom.get());
        contact.setGenre(genre.get());
        contact.setDateNaissance(dateNaissance.get());
        contact.setPseudonyme(pseudonyme.get());
        contact.setAdresse(adresse.get());
        contact.setTelPerso(telPerso.get());
        contact.setTelPro(telPro.get());
        contact.setEmail(email.get());
        contact.setLienCode(lienCode.get());
        return contact;
    }

    private void configurerValidation()
        {
            nom.addListener((obs, old, val) -> valider());
            prenom.addListener((obs, old, val) -> valider());
            genre.addListener((obs, old, val) -> valider());
            adresse.addListener((obs, old, val) -> valider());
            telPerso.addListener((obs, old, val) -> valider());
            telPro.addListener((obs, old, val) -> valider());
            email.addListener((obs, old, val) -> valider());
            pseudonyme.addListener((obs, old, val) -> valider());
            lienCode.addListener((obs, old, val) -> valider());
            dateNaissance.addListener((obs, old, val) -> valider());
        }


    private void valider() {
        Contact temp = new Contact();
        temp.setNom(nom.get());
        temp.setPrenom(prenom.get());
        temp.setGenre(genre.get());
        temp.setDateNaissance(dateNaissance.get());
        temp.setPseudonyme(pseudonyme.get());
        temp.setAdresse(adresse.get());
        temp.setTelPerso(telPerso.get());
        temp.setTelPro(telPro.get());
        temp.setEmail(email.get());
        temp.setLienCode(lienCode.get());

        ValidationResult result = ContactValidator.validateContact(temp);
        formulaireValide.set(result.isValide());
        messageErreur.set(result.getMessageErreur());
    }

    private void viderFormulaire() {
        nom.set("");
        prenom.set("");
        genre.set("");
        dateNaissance.set(null);
        pseudonyme.set("");
        adresse.set("");
        telPerso.set("");
        telPro.set("");
        email.set("");
        lienCode.set("");
    }

    public boolean isFormulaireValide() {
        return formulaireValideProperty().get();
    }
    public String getMessageErreur() {
        return messageErreurProperty().get();
    }


    // Expose les properties au form
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty genreProperty() { return genre; }
    public ObjectProperty<LocalDate> dateNaissanceProperty() { return dateNaissance; }
    public StringProperty pseudonymeProperty() { return pseudonyme; }
    public StringProperty adresseProperty() { return adresse; }
    public StringProperty telPersoProperty() { return telPerso; }
    public StringProperty telProProperty() { return telPro; }
    public StringProperty emailProperty() { return email; }
    public StringProperty lienCodeProperty() { return lienCode; }
    public BooleanProperty formulaireValideProperty() { return formulaireValide; }
    public StringProperty messageErreurProperty() { return messageErreur; }
}

package fr.afpa.javacard.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class Contact {

    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();
    private final StringProperty lienGitHub = new SimpleStringProperty();
    private final StringProperty lienGitLab = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> dateCreation = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dateModification = new SimpleObjectProperty<>();

    public Contact() {
        this.dateCreation.set(LocalDateTime.now());
        this.dateModification.set(LocalDateTime.now());
    }

    public Contact(String nom, String prenom) {
        this();
        this.nom.set(nom);
        this.prenom.set(prenom);
    }

    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty emailProperty() { return email; }
    public StringProperty telephoneProperty() { return telephone; }
    public StringProperty adresseProperty() { return adresse; }
    public StringProperty lienGitHubProperty() { return lienGitHub; }
    public StringProperty lienGitLabProperty() { return lienGitLab; }
    public ObjectProperty<LocalDateTime> dateCreationProperty() { return dateCreation; }
    public ObjectProperty<LocalDateTime> dateModificationProperty() { return dateModification; }

    //Convert properties into String
    @Override
    public String toString() {
        return String.format("%s %s",
                nom.get() != null ? nom.get() : "",
                prenom.get() != null ? prenom.get() : "").trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return Objects.equals(nom.get(), contact.nom.get()) &&
                Objects.equals(prenom.get(), contact.prenom.get()) &&
                Objects.equals(email.get(), contact.email.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom.get(), prenom.get(), email.get());
    }

    public void setAsModified() {
        this.dateModification.set(LocalDateTime.now());
    }
}

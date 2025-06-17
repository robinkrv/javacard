package fr.afpa.javacard.models;

import javafx.beans.property.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Contact implements Serializable {

    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateNaissance = new SimpleObjectProperty<>();
    private final StringProperty pseudonyme = new SimpleStringProperty();
    private final StringProperty adresse = new SimpleStringProperty();
    private final StringProperty telPerso = new SimpleStringProperty();
    private final StringProperty telPro = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty lienCode = new SimpleStringProperty();

    public Contact() {}

    public Contact(String nom, String prenom) {
        this.nom.set(nom);
        this.prenom.set(prenom);
    }

    // Properties
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty genreProperty() { return genre; }
    public ObjectProperty<LocalDate> dateNaissanceProperty() { return dateNaissance; }
    public StringProperty pseudoProperty() { return pseudonyme; }
    public StringProperty adresseProperty() { return adresse; }
    public StringProperty telPersoProperty() { return telPerso; }
    public StringProperty telProProperty() { return telPro; }
    public StringProperty emailProperty() { return email; }
    public StringProperty lienCodeProperty() { return lienCode; }

    // Getters/Setters
    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }
    public String getGenre() { return genre.get(); }
    public void setGenre(String genre) { this.genre.set(genre); }
    public LocalDate getDateNaissance() { return dateNaissance.get(); }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance.set(dateNaissance); }
    public String getPseudonyme() { return pseudonyme.get(); }
    public void setPseudonyme(String pseudo) { this.pseudonyme.set(pseudo); }
    public String getAdresse() { return adresse.get(); }
    public void setAdresse(String adresse) { this.adresse.set(adresse); }
    public String getTelPerso() { return telPerso.get(); }
    public void setTelPerso(String telPerso) { this.telPerso.set(telPerso); }
    public String getTelPro() { return telPro.get(); }
    public void setTelPro(String telPro) { this.telPro.set(telPro); }
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public String getLienCode() { return lienCode.get(); }
    public void setLienCode(String lienCode) { this.lienCode.set(lienCode); }

    @Override
    public String toString() {
        return (nom.get() != null ? nom.get() : "") + " " + (prenom.get() != null ? prenom.get() : "");
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
}

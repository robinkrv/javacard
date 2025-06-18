package fr.afpa.javacard.dto;

import java.time.LocalDate;

public class ContactDTO {
    private String nom;
    private String prenom;
    private String genre;
    private LocalDate dateNaissance;
    private String pseudonyme;
    private String adresse;
    private String telPerso;
    private String telPro;
    private String email;
    private String lienCode;

    public String getPseudonyme() {
        return pseudonyme;
    }

    public void setPseudonyme(String pseudonyme) {
        this.pseudonyme = pseudonyme;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelPerso() {
        return telPerso;
    }

    public void setTelPerso(String telPerso) {
        this.telPerso = telPerso;
    }

    public String getTelPro() {
        return telPro;
    }

    public void setTelPro(String telPro) {
        this.telPro = telPro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLienCode() {
        return lienCode;
    }

    public void setLienCode(String lienCode) {
        this.lienCode = lienCode;
    }
}


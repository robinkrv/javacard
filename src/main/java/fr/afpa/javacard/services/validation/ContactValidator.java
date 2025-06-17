package fr.afpa.javacard.services.validation;

import fr.afpa.javacard.enums.ChampValidation;
import fr.afpa.javacard.models.Contact;

import java.util.*;
import java.util.regex.Pattern;

public class ContactValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String TELEPHONE_REGEX = "^(?:\\+33|0)[1-9](?:[.-]?\\d{2}){4}$";
    private static final String GITHUB_REGEX = "^https://github\\.com/[a-zA-Z0-9._-]+$";
    private static final String GITLAB_REGEX = "^https://gitlab\\.com/[a-zA-Z0-9._-]+$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern TELEPHONE_PATTERN = Pattern.compile(TELEPHONE_REGEX);
    private static final Pattern GITHUB_PATTERN = Pattern.compile(GITHUB_REGEX);
    private static final Pattern GITLAB_PATTERN = Pattern.compile(GITLAB_REGEX);


    /**
     * Validation individuelle d'un champ avec résultat détaillé
     * @param champ Le type de champ
     * @param valeur La valeur du champ
     * @return ValidationResult avec erreur spécifique
     */
    public static ValidationResult validateField(ChampValidation champ, String valeur) {
        List<String> erreurs = new ArrayList<>();
        Set<ChampValidation> champsInvalides = new HashSet<>();

        if (!isFieldValid(champ, valeur)) {
            String message = getFieldErrorMessage(champ, valeur);
            erreurs.add(message);
            champsInvalides.add(champ);
        }

        boolean valide = erreurs.isEmpty();
        return new ValidationResult(valide, erreurs, champsInvalides);
    }

    /**
     * Valide un contact complet
     * @param contact Le contact à valider
     * @return ValidationResult avec tous les champs invalides
     */
    public static ValidationResult validateContact(Contact contact) {
        if (contact == null) {
            return ValidationResult.echec(
                    Arrays.asList("Le contact ne peut pas être null"),
                    new HashSet<>()
            );
        }

        List<String> erreurs = new ArrayList<>();
        Set<ChampValidation> champsInvalides = new HashSet<>();

        String nom = contact.nomProperty().get();
        if (!isFieldValid(ChampValidation.NOM, nom)) {
            erreurs.add("Le nom est obligatoire (minimum 2 caractères)");
            champsInvalides.add(ChampValidation.NOM);
        }

        String prenom = contact.prenomProperty().get();
        if (!isFieldValid(ChampValidation.PRENOM, prenom)) {
            erreurs.add("Le prénom est obligatoire (minimum 2 caractères)");
            champsInvalides.add(ChampValidation.PRENOM);
        }

        String genre = contact.genreProperty().get();
        if (!isFieldValid(ChampValidation.GENRE, genre)) {
            erreurs.add("Le genre est obligatoire (valeurs acceptées : Homme, Femme, Non-binaire)");
            champsInvalides.add(ChampValidation.GENRE);
        }

        String email = contact.emailProperty().get();
        if (!isFieldValid(ChampValidation.EMAIL, email)) {
            erreurs.add("Format d'email invalide");
            champsInvalides.add(ChampValidation.EMAIL);
        }

        String telPerso = contact.telPersoProperty().get();
        if (!isFieldValid(ChampValidation.TELEPHONE_PERSO, telPerso)) {
            erreurs.add("Téléphone personnel obligatoire (format français attendu)");
            champsInvalides.add(ChampValidation.TELEPHONE_PERSO);
        }
        String telPro = contact.telProProperty().get();
        if (!isFieldValid(ChampValidation.TELEPHONE_PRO, telPro)) {
            erreurs.add("Format de téléphone professionnel invalide (format français attendu)");
            champsInvalides.add(ChampValidation.TELEPHONE_PRO);
        }

        String adresse = contact.adresseProperty().get();
        if (!isFieldValid(ChampValidation.ADRESSE, adresse)) {
            erreurs.add("L'adresse est trop courte (minimum 10 caractères)");
            champsInvalides.add(ChampValidation.ADRESSE);
        }

        String github = contact.lienCodeProperty().get();
        if (!isFieldValid(ChampValidation.GITHUB, github)) {
            erreurs.add("Format URL GitHub invalide");
            champsInvalides.add(ChampValidation.GITHUB);
        }

        boolean valide = erreurs.isEmpty();
        return new ValidationResult(valide, erreurs, champsInvalides);
    }

    /**
     * Méthode helper pour valider un champ spécifique, seule responsable de la logique de validation
     * @param champ Le type de champ à valider
     * @param valeur La valeur du champ
     * @return true si valide, false sinon
     */
    private static boolean isFieldValid(ChampValidation champ, String valeur) {
        if (valeur == null) valeur = "";

        switch (champ) {
            case NOM:
                return validerNom(valeur);
            case PRENOM:
                return validerPrenom(valeur);
            case GENRE:
                return validerGenre(valeur);
            case EMAIL:
                return validerEmail(valeur);
            case TELEPHONE_PERSO:
                return validerTelephonePerso(valeur);
            case TELEPHONE_PRO:
                return validerTelephone(valeur);
            case ADRESSE:
                return validerAdresse(valeur);
            case GITHUB:
                return validerGithub(valeur);
            case GITLAB:
                return validerGitlab(valeur);
            default:
                return true;
        }
    }

    /**
     * Obtient le message d'erreur pour un champ spécifique,
     * @param champ Le type de champ
     * @param valeur La valeur invalide
     * @return Message d'erreur adapté
     */
    private static String getFieldErrorMessage(ChampValidation champ, String valeur) {
        if (valeur == null) valeur = "";

        switch (champ) {
            case NOM:
                return valeur.trim().isEmpty() ? "Le nom est obligatoire" : "Le nom doit contenir au moins 2 caractères";
            case PRENOM:
                return valeur.trim().isEmpty() ? "Le prénom est obligatoire" : "Le prénom doit contenir au moins 2 caractères";
            case EMAIL:
                return valeur.trim().isEmpty() ? "L'email ne peut pas être vide" : "Format d'email invalide";
            case TELEPHONE_PERSO:
                return valeur.trim().isEmpty() ? "Le téléphone ne peut pas être vide" : "Format de téléphone invalide (format français attendu)";
            case ADRESSE:
                return valeur.trim().isEmpty() ? "L'adresse ne peut pas être vide" : "L'adresse est trop courte (minimum 10 caractères)";
            case GITHUB:
                return valeur.trim().isEmpty() ? "L'URL GitHub ne peut pas être vide" : "Format URL GitHub invalide";
            case GITLAB:
                return valeur.trim().isEmpty() ? "L'URL GitLab ne peut pas être vide" : "Format URL GitLab invalide";
            default:
                return "Champ invalide";
        }
    }

    private static boolean validerNom(String nom) {
        return nom != null && !nom.trim().isEmpty() && nom.trim().length() >= 2;
    }

    private static boolean validerPrenom(String prenom) {
        return prenom != null && !prenom.trim().isEmpty() && prenom.trim().length() >= 2;
    }

    private static boolean validerGenre(String genre) {
        if (genre == null) return false;
        String val = genre.trim().toLowerCase();
        return val.equals("homme") || val.equals("femme") || val.equals("non-binaire");
    }

    private static boolean validerEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    private static boolean validerTelephonePerso(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return false; // Obligatoire !
        }
        return TELEPHONE_PATTERN.matcher(telephone.trim()).matches();
    }


    private static boolean validerTelephone(String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return true;
        }
        return TELEPHONE_PATTERN.matcher(telephone.trim()).matches();
    }

    private static boolean validerAdresse(String adresse) {
        if (adresse == null || adresse.trim().isEmpty()) {
            return false;
        }
        return adresse.trim().length() >= 10;
    }

    private static boolean validerGithub(String github) {
        if (github == null || github.trim().isEmpty()) {
            return true;
        }
        return GITHUB_PATTERN.matcher(github.trim()).matches();
    }

    private static boolean validerGitlab(String gitlab) {
        if (gitlab == null || gitlab.trim().isEmpty()) {
            return true;
        }
        return GITLAB_PATTERN.matcher(gitlab.trim()).matches();
    }
}

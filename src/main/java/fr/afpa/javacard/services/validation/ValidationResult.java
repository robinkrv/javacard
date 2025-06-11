package fr.afpa.javacard.services.validation;

import fr.afpa.javacard.enums.ChampValidation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidationResult {
    private final boolean valide;
    private final List<String> erreurs;
    private final Set<ChampValidation> champsInvalides;

    public ValidationResult(boolean valide, List<String> erreurs, Set<ChampValidation> champsInvalides) {
        this.valide = valide;
        this.erreurs = erreurs != null ? new ArrayList<>(erreurs) : new ArrayList<>();
        this.champsInvalides = champsInvalides != null ? new HashSet<>(champsInvalides) : new HashSet<>();
    }

    public ValidationResult(boolean valide, List<String> erreurs) {
        this(valide, erreurs, new HashSet<>());
    }

    public boolean isValide() { return valide; }
    public List<String> getErreurs() { return new ArrayList<>(erreurs); }
    public String getMessageErreur() { return String.join("\n", erreurs); }

    public Set<ChampValidation> getChampsInvalides() {
        return new HashSet<>(champsInvalides);
    }

    public boolean isChampInvalide(ChampValidation champ) {
        return champsInvalides.contains(champ);
    }

    public static ValidationResult succes() {
        return new ValidationResult(true, new ArrayList<>(), new HashSet<>());
    }

    public static ValidationResult echec(List<String> erreurs, Set<ChampValidation> champs) {
        return new ValidationResult(false, erreurs, champs);
    }

    public static ValidationResult echec(List<String> erreurs) {
        return new ValidationResult(false, erreurs, new HashSet<>());
    }
}

package fr.afpa.javacard.controllers;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.viewmodels.ContactFormViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ContactFormController {

    @FXML private TextField nomField, prenomField, genreField, pseudoField, adresseField,
            telPersoField, telProField, emailField, lienCodeField;
    @FXML private DatePicker dateNaissancePicker;
    @FXML private Button validerBtn, annulerBtn;
    @FXML private Label validationText;

    private final ContactFormViewModel viewModel = new ContactFormViewModel();
    private Consumer<Contact> onContactCree;

    @FXML
    public void initialize() {
        nomField.textProperty().bindBidirectional(viewModel.nomProperty());
        prenomField.textProperty().bindBidirectional(viewModel.prenomProperty());
        genreField.textProperty().bindBidirectional(viewModel.genreProperty());
        pseudoField.textProperty().bindBidirectional(viewModel.pseudonymeProperty());
        adresseField.textProperty().bindBidirectional(viewModel.adresseProperty());
        telPersoField.textProperty().bindBidirectional(viewModel.telPersoProperty());
        telProField.textProperty().bindBidirectional(viewModel.telProProperty());
        emailField.textProperty().bindBidirectional(viewModel.emailProperty());
        lienCodeField.textProperty().bindBidirectional(viewModel.lienCodeProperty());

        dateNaissancePicker.valueProperty().bindBidirectional(viewModel.dateNaissanceProperty());

        validationText.textProperty().unbind(); // (au cas où)
        validationText.setText(""); // Rien au démarrage !

        validationText.setStyle("-fx-text-fill: red;");
        viewModel.formulaireSoumisProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // Affiche en live l’erreur actuelle dès qu'un champ change
                viewModel.messageErreurProperty().addListener((o2, ov, nv) -> {
                    validationText.setText(nv);
                });
            }
        });
    }

    @FXML
    public void onValider() {
        viewModel.setSoumis(true); // indique qu'on a tenté de soumettre
        if (!viewModel.isFormulaireValide()) {
            String msg = viewModel.getMessageErreur();
            validationText.setText(msg);
            validationText.setTooltip(new Tooltip(msg)); // <-- Le tooltip dynamique ici
            return; // NE PAS fermer la fenêtre
        } else {
            validationText.setTooltip(null); // enlève la tooltip si plus d'erreur
        }
        // Si tout est bon :
        validationText.setText(""); // Efface l’éventuel ancien message
        Contact contact = viewModel.sauvegarder();
        if (onContactCree != null) {
            onContactCree.accept(contact);
        }
        ((Stage) validerBtn.getScene().getWindow()).close();
    }



    @FXML
    public void onAnnuler() {
        ((Stage) annulerBtn.getScene().getWindow()).close();
    }

    public void setOnContactCree(Consumer<Contact> consumer) {
        this.onContactCree = consumer;
    }

    public ContactFormViewModel getViewModel() {
        return viewModel;
    }
}

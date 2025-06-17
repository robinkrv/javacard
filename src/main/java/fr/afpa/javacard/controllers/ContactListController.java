package fr.afpa.javacard.controllers;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.viewmodels.ContactFormViewModel;
import fr.afpa.javacard.viewmodels.ContactListViewModel;
import fr.afpa.javacard.services.ContactService;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class ContactListController {

    @FXML private TableView<Contact> contactTable;
    @FXML private TableColumn<Contact, String> nomCol;
    @FXML private TableColumn<Contact, String> prenomCol;
    @FXML private TableColumn<Contact, Void> modifierCol;
    @FXML private TableColumn<Contact, Void> supprimerCol;
    @FXML private TextField rechercherField;
    @FXML private Button creerBtn;
    @FXML private Button exporterBtn;

    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label genreLabel;
    @FXML private Label dateNaissanceLabel;
    @FXML private Label pseudoLabel;
    @FXML private Label adresseLabel;
    @FXML private Label telPersoLabel;
    @FXML private Label telProLabel;
    @FXML private Label emailLabel;
    @FXML private Label lienCodeLabel;


    private final ContactListViewModel viewModel = new ContactListViewModel(new ContactService());

    @FXML
    public void initialize() {
        // Colonnes
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        addEditButtonToTable();
        addDeleteButtonToTable();

        // Liaison TableView (data + selection)
        contactTable.setItems(viewModel.getSortedContacts());
        viewModel.contactSelectionneProperty().bind(contactTable.getSelectionModel().selectedItemProperty());
        contactTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            afficherDetailContact(selected);
        });

        // Recherche (binding direct ViewModel)
        rechercherField.textProperty().bindBidirectional(viewModel.rechercheProperty());

        // Création
        creerBtn.setOnAction(evt -> ouvrirPopupContact(null));

        exporterBtn.setOnAction(evt -> ouvrirPopupExport());

        // Détails initial : rien sélectionné
        afficherDetailContact(null);
    }

    // ---- boutons colonne edition/suppression ----
    private void addEditButtonToTable() {
        modifierCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            {
                ImageView iv = new ImageView(getClass().getResource("/icons/edit-icon.png").toString());
                iv.setPreserveRatio(true); // évite les images écrasées
                iv.setFitHeight(18);  // Laisse à 18 ou 20 pixels (ajuste au besoin : 16..20 est pas mal)
                iv.setFitWidth(18);
                btn.setGraphic(iv);
                btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 2"); // Style optionnel + padding mini
                btn.setOnAction(evt -> {
                    Contact c = getTableView().getItems().get(getIndex());
                    ouvrirPopupContact(c);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
    private void addDeleteButtonToTable() {
        supprimerCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();
            {
                ImageView iv = new ImageView(getClass().getResource("/icons/delete-icon.png").toString());
                iv.setPreserveRatio(true); // évite les images écrasées
                iv.setFitHeight(18);  // Même taille que l’autre bouton
                iv.setFitWidth(18);
                btn.setGraphic(iv);
                btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 2"); // Optionnel mais conseillé
                btn.setOnAction(evt -> {
                    Contact c = getTableView().getItems().get(getIndex());
                    if (confirmSuppression(c)) {
                        viewModel.supprimerContactSelectionne();
                        contactTable.getSelectionModel().clearSelection();
                        afficherDetailContact(null);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }


    private void ouvrirPopupContact(Contact contact) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/afpa/javacard/create-modify-view.fxml"));
            Parent root = loader.load();
            ContactFormController formCtrl = loader.getController();

            boolean modeEdition = (contact != null);

            // Bind du formulaire : mode édition ou ajout
            formCtrl.getViewModel().chargerContact(contact); // ça gère déjà null

            formCtrl.setOnContactCree(contactCreeOuModif -> {
                if (modeEdition) {
                    // On modifie le contact existant
                    viewModel.getContactService().modifierContact(contact, contactCreeOuModif);
                } else {
                    // Nouveau contact
                    viewModel.getContactService().ajouterContact(contactCreeOuModif);
                }
                contactTable.refresh();
                afficherDetailContact(contactCreeOuModif);
            });

            Stage popup = new Stage();
            popup.setScene(new Scene(root));
            popup.setTitle(modeEdition ? "Modifier le contact" : "Créer un contact");
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void ouvrirPopupExport() {
        try {
            // Remplace le chemin ci-dessous par le chemin réel de ton FXML export
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/afpa/javacard/export-view.fxml"));
            Parent root = loader.load();

            // Si tu veux passer des contacts sélectionnés au popup :
            // ExportPopupController ctrl = loader.getController();
            // ctrl.setContactsAExporter(contactTable.getSelectionModel().getSelectedItems());

            Stage popup = new Stage();
            popup.setScene(new Scene(root));
            popup.setTitle("Exporter les contacts");
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // ---- Affichage détail sur la droite ----
    private void afficherDetailContact(Contact c) {
        nomLabel.setText(c != null ? c.nomProperty().get() : "");
        prenomLabel.setText(c != null ? c.prenomProperty().get() : "");
        genreLabel.setText(c != null ? c.genreProperty().get() : "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateNaissanceLabel.setText(
                c != null && c.dateNaissanceProperty().get() != null
                        ? c.dateNaissanceProperty().get().format(formatter)
                        : ""
        );
        pseudoLabel.setText(c != null ? c.pseudoProperty().get() : "");
        adresseLabel.setText(c != null ? c.adresseProperty().get() : "");
        telPersoLabel.setText(c != null ? c.telPersoProperty().get() : "");
        telProLabel.setText(c != null ? c.telProProperty().get() : "");
        emailLabel.setText(c != null ? c.emailProperty().get() : "");
    }

    // ---- Confirmation suppression ----
    private boolean confirmSuppression(Contact c) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Supprimer ce contact ?\n" + c.nomProperty().get() + " " + c.prenomProperty().get(),
                ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Confirmation suppression");
        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }
}

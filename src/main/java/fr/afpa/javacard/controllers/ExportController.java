package fr.afpa.javacard.controllers;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.services.ContactService;
import fr.afpa.javacard.services.export.CSVExportService;
import fr.afpa.javacard.services.export.JSONExportService;
import fr.afpa.javacard.services.export.VCardExportService;
import fr.afpa.javacard.utils.ExportService;
import fr.afpa.javacard.viewmodels.ExportViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExportController {
    @FXML private RadioButton csvRadio;
    @FXML private RadioButton jsonRadio;
    @FXML private RadioButton vcardRadio;
    @FXML private Button exporterBtn;
    @FXML private Button annulerBtn;
    @FXML private ListView<Contact> contactsListView;
    @FXML private ListView<Contact> selectionnesListView;

    private final Map<String, ExportService> exportServices = new HashMap<>();
    private ExportViewModel exportViewModel;

    public ExportController() {
        // Map tous tes services
        exportServices.put("CSV", new CSVExportService());
        exportServices.put("JSON", new JSONExportService());
        exportServices.put("VCARD", new VCardExportService());
    }

    @FXML
    public void initialize() {
        // Init ViewModel
        ExportService initialService = exportServices.get("CSV");
        exportViewModel = new ExportViewModel(new ContactService(), initialService);

        // Bind RadioButtons
        csvRadio.setUserData("CSV");
        jsonRadio.setUserData("JSON");
        vcardRadio.setUserData("VCARD");
        ToggleGroup group = new ToggleGroup();
        csvRadio.setToggleGroup(group);
        jsonRadio.setToggleGroup(group);
        vcardRadio.setToggleGroup(group);
        csvRadio.setSelected(true);

        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String format = newVal.getUserData().toString();
                exportViewModel.setFormatSelectionne(format);
            }
        });

        // Bind ListViews <---
        contactsListView.setItems(exportViewModel.getContactsDisponibles());
        selectionnesListView.setItems(exportViewModel.getContactsSelectionnes());

        // UX : affichage jolis noms (nom/prenom)
        contactsListView.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Contact c, boolean empty) {
                super.updateItem(c, empty);
                setText((empty || c == null) ? "" : c.getPrenom() + " " + c.getNom());
            }
        });
        selectionnesListView.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Contact c, boolean empty) {
                super.updateItem(c, empty);
                setText((empty || c == null) ? "" : c.getPrenom() + " " + c.getNom());
            }
        });

        // Click gauche : ajouter dans sélection
        contactsListView.setOnMouseClicked(event -> {
            Contact sel = contactsListView.getSelectionModel().getSelectedItem();
            if (sel != null && !exportViewModel.getContactsSelectionnes().contains(sel)) {
                exportViewModel.ajouterContact(sel);
            }
        });

        // Click droit : retire de la sélection
        selectionnesListView.setOnMouseClicked(event -> {
            Contact sel = selectionnesListView.getSelectionModel().getSelectedItem();
            if (sel != null) {
                exportViewModel.retirerContact(sel);
            }
        });

        // Export
        exporterBtn.setOnAction(event -> exporterCurrentSelection());
        annulerBtn.setOnAction(event -> fermerPopup());
    }

    private void exporterCurrentSelection() {
        // Choix du format
        String format = exportViewModel.formatSelectionneProperty().get();
        ExportService selectedService = exportServices.get(format);
        if (selectedService == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Format d'export inconnu : " + format);
            alert.showAndWait();
            return;
        }
        exportViewModel.setExportService(selectedService);


        FileChooser fc = new FileChooser();
        fc.setTitle("Choisissez le fichier d’export");
        switch (format) {
            case "CSV":   fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv")); break;
            case "JSON":  fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json")); break;
            case "VCARD": fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("VCard", "*.vcf")); break;
        }
        File file = fc.showSaveDialog(exporterBtn.getScene().getWindow());
        if (file != null) {
            exportViewModel.exporterContactsSelectionnes(file);
            String msg = exportViewModel.getMessageStatut();
            if (msg.startsWith("❌")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, msg);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Export terminé !");
                alert.showAndWait();
            }
            fermerPopup();
        }
    }

    private void fermerPopup() {
        try { annulerBtn.getScene().getWindow().hide(); } catch (Exception ignored) {}
    }
}

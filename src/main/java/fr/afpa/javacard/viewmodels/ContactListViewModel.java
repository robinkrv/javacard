package fr.afpa.javacard.viewmodels;

import fr.afpa.javacard.models.Contact;
import fr.afpa.javacard.services.ContactService;
import javafx.beans.property.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class ContactListViewModel {

    private final ContactService contactService;
    private final FilteredList<Contact> filteredContacts;
    private final SortedList<Contact> sortedContacts;

    private final StringProperty recherche = new SimpleStringProperty("");
    private final ObjectProperty<Contact> contactSelectionne = new SimpleObjectProperty<>();
    private final BooleanProperty contactSelectionneValide = new SimpleBooleanProperty(false);

    public ContactListViewModel(ContactService contactService) {
        this.contactService = contactService;

        this.filteredContacts = new FilteredList<>(contactService.getContacts());
        this.sortedContacts = new SortedList<>(filteredContacts);

        setupFilters();
    }

    private void setupFilters() {

        recherche.addListener((obs, oldVal, newVal) -> {
            filteredContacts.setPredicate(contact -> {
                if (newVal == null || newVal.trim().isEmpty()) {
                    return true;
                }

                String critere = newVal.toLowerCase().trim();
                return contact.nomProperty().get().toLowerCase().contains(critere) ||
                        contact.prenomProperty().get().toLowerCase().contains(critere) ||
                        contact.emailProperty().get().toLowerCase().contains(critere);
            });
        });

        contactSelectionne.addListener((obs, oldVal, newVal) ->
                contactSelectionneValide.set(newVal != null)
        );
    }

    public void supprimerContactSelectionne() {
        Contact contact = contactSelectionne.get();
        if (contact != null) {
            contactService.supprimerContact(contact);
        }
    }

    public StringProperty rechercheProperty() { return recherche; }
    public ObjectProperty<Contact> contactSelectionneProperty() { return contactSelectionne; }
    public BooleanProperty contactSelectionneValideProperty() { return contactSelectionneValide; }
    public SortedList<Contact> getSortedContacts() { return sortedContacts; }
}


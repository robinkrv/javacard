package fr.afpa.javacard.services.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.afpa.javacard.dto.ContactDTO;
import fr.afpa.javacard.models.Contact;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataPersistenceService {

    private final File fichier = new File("contacts.json");
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public boolean sauvegarderContacts(List<Contact> contacts) {
        try {
            List<ContactDTO> dtos = contacts.stream()
                    .map(Contact::toDTO)
                    .collect(Collectors.toList());
            mapper.writerWithDefaultPrettyPrinter().writeValue(fichier, dtos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Contact> chargerContacts() {
        if (!fichier.exists()) return new ArrayList<>(); // Ne retourne jamais null !
        try {
            CollectionType type = mapper.getTypeFactory()
                    .constructCollectionType(List.class, ContactDTO.class);
            List<ContactDTO> dtos = mapper.readValue(fichier, type);
            return dtos.stream()
                    .map(Contact::fromDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

package fr.afpa.javacard.services.persistence;

import fr.afpa.javacard.models.Contact;
import java.io.*;
import java.util.List;

public class DataPersistenceService {

    private final File fichier = new File("contacts.dat");

    public boolean sauvegarderContacts(List<Contact> contacts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(contacts);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Contact> chargerContacts() {
        if (!fichier.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
            return (List<Contact>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

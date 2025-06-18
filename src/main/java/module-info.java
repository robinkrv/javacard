module fr.afpa.javacard {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    opens fr.afpa.javacard to javafx.fxml;
    opens fr.afpa.javacard.models to javafx.base, javafx.fxml;
    opens fr.afpa.javacard.dto to com.fasterxml.jackson.databind;

    opens fr.afpa.javacard.controllers to javafx.fxml;
    exports fr.afpa.javacard;
}
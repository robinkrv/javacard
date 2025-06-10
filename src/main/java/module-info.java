module fr.afpa.javacard {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens fr.afpa.javacard to javafx.fxml;
    exports fr.afpa.javacard;
}
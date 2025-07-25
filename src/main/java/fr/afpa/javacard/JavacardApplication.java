package fr.afpa.javacard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JavacardApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavacardApplication.class.getResource("contact-list-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600,400 );
        stage.setTitle("Javacard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
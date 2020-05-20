package com.vonkez;

import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import com.vonkez.source.SourceManager;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

import static org.kordamp.ikonli.ionicons4.Ionicons4Material.SETTINGS;


public class Main extends Application {
    public static void main(String[] args) throws IOException {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        SourceManager.initialize();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Parent mainNode = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
        JFXDecorator decorator = new JFXDecorator(stage, mainNode);
        decorator.setGraphic(new FontIcon(SETTINGS));



        Scene scene = new Scene(decorator);
//        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            System.out.println("Key pressed");
//            System.out.println(event.getText());
//            event.consume();
//        });
        //scene.setOnKeyPressed(event -> System.out.println(event.getText()));
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                           JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                           getClass().getResource("css/styles.css").toExternalForm());
        stage.setTitle("MangaApp");
        stage.setScene(scene);
        stage.show();
    }
}

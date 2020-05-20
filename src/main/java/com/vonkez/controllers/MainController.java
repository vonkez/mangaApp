package com.vonkez.controllers;

import com.jfoenix.controls.JFXButton;
import com.vonkez.model.Manga;
import com.vonkez.ui.LibraryCell;
import com.vonkez.ui.LibraryManager;
import com.vonkez.ui.NavigationManager;
import com.vonkez.utils.SearchboxManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private StackPane mainStackpane;
    @FXML
    private StackPane contentPane;
    @FXML
    private VBox firstColumn;
    @FXML
    private VBox secondColumn;
    @FXML
    private GridPane mainGrid;
    // Menu buttons


    @FXML
    private JFXButton libraryButton;

    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXButton narutoButton;
    @FXML
    private Button pdButton;
    @FXML
    private ListView libraryListView;
    @FXML
    private Button spaceButton;
    @FXML
    private GridPane drawerPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationManager.initialize(contentPane);
        mainGrid.toFront();

        // Set up library search
        TextField librarySearchBox = TextFields.createClearableTextField();
        VBox.setMargin(librarySearchBox, new Insets(10));
        secondColumn.getChildren().add(0,librarySearchBox);
        librarySearchBox.setOnAction(event -> {
            SearchboxManager.search(((TextField)event.getSource()).getText());
        });

        // Set up Library
        libraryListView.setCellFactory(new Callback<ListView<Manga>, ListCell<Manga>>() {
            @Override
            public ListCell<Manga> call(ListView<Manga> param) {
                return new LibraryCell();
            }
        });
        libraryListView.setItems(LibraryManager.getLibrary());


        /*
        var c = new Circle(25);
        c.setFill(new ImagePattern(new Image("https://lithi.io/file/9MQ0.png")));

        f.setFill(new ImagePattern(new Image("https://lithi.io/file/9MQ0.png")));
        pdButton.setGraphic(f);
        */


    }


}

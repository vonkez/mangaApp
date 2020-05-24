package com.vonkez.controllers;

import com.jfoenix.controls.JFXButton;
import com.vonkez.database.MangaDaoSQLite;
import com.vonkez.model.Manga;
import com.vonkez.ui.LibraryCell;
import com.vonkez.ui.LibraryManager;
import com.vonkez.ui.NavigationManager;
import com.vonkez.utils.SearchboxManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML
    private StackPane contentPane;
    @FXML
    private VBox firstColumn;
    @FXML
    private VBox secondColumn;
    @FXML
    private GridPane mainGrid;
    @FXML
    private ListView libraryListView;
    @FXML
    public Button backButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationManager.initialize(contentPane);
        mainGrid.toFront();
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(20));

        // Set up library search
        TextField librarySearchBox = TextFields.createClearableTextField();
        librarySearchBox.getStyleClass().add("library-search-box");
        librarySearchBox.setPromptText("Search");
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
        libraryListView.setItems(LibraryManager.getLibraryMangas());


        backButton.setOnAction(event -> {
            NavigationManager.navigateBack();
            System.out.println("click");
        });

        // listen mouse for back button visibility
        contentPane.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            if (event.getX()<220 && event.getY()<220 && event.getX()>0 && NavigationManager.canNavigateBack()) {
                backButton.setVisible(true);
            }
            else {
                backButton.setVisible(false);
            }

//            System.out.println("x: " + event.getX());
//            System.out.println("y: " + event.getY());
        });


        /*
        var c = new Circle(25);
        c.setFill(new ImagePattern(new Image("https://lithi.io/file/9MQ0.png")));

        f.setFill(new ImagePattern(new Image("https://lithi.io/file/9MQ0.png")));
        pdButton.setGraphic(f);
        */


    }


}

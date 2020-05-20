package com.vonkez.ui;

import com.jfoenix.controls.JFXButton;
import com.vonkez.model.Manga;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class LibraryCell extends ListCell<Manga> {
    JFXButton button = new JFXButton("");
    Circle c = new Circle(25);
    Manga currentManga;

    public LibraryCell() {

        setGraphic(button);
        button.setPrefWidth(240);
        button.setMaxWidth(240);
        button.setGraphic(c);
        button.setGraphicTextGap(20);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setOnAction(event -> {
            NavigationManager.navigate(currentManga);
        });
    }

    @Override
    protected void updateItem(Manga item, boolean empty) {
        currentManga = item;
        super.updateItem(item, empty);
        if (item != null) {
            button.setText(item.title);
            c.setFill(new ImagePattern(new Image(item.thumbnailUrl)));
        }
    }
}

package com.vonkez.ui;

import com.jfoenix.controls.JFXButton;
import com.vonkez.model.Manga;
import io.reactivex.Observable;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class LibraryCell extends ListCell<Manga> {
    JFXButton button = new JFXButton("");
    Circle circle = new Circle(25);
    Manga currentManga;

    public LibraryCell() {

        //setGraphic(button);
        button.setPrefWidth(240);
        button.setMaxWidth(240);
        button.setGraphic(circle);
        button.setGraphicTextGap(20);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setOnAction(event -> {
            NavigationManager.navigate(currentManga);
        });
    }

    @Override
    protected void updateItem(Manga item, boolean empty) {
        super.updateItem(item, empty);
        currentManga = item;
        if (item != null) {
            setGraphic(button);
            button.setText(item.title);
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .map(x -> {
                        return item.fetchThumbnail(item.thumbnailUrl);
                    })
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(image -> circle.setFill(new ImagePattern(image)));

        }
        else{
            setGraphic(null);
        }
    }
}

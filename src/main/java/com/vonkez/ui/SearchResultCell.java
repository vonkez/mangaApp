package com.vonkez.ui;

import com.jfoenix.controls.JFXButton;
import com.vonkez.model.MangasPage;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.rxjavafx.transformers.FxObservableTransformers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SearchResultCell extends ListCell<Observable<MangasPage>> {
    VBox mainContainer;
    FlowPane mangaContainer;
    Label containerName;
    DropShadow dropShadow;
    public SearchResultCell() {
        mainContainer = new VBox();
        mangaContainer = new FlowPane();
        containerName = new Label();
        dropShadow = new DropShadow(35, Color.rgb(22,22,22, 0.5));

        containerName.setStyle("-fx-font-size: 50; -fx-text-fill: white;");


        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setEffect(dropShadow);
        mainContainer.setStyle("-fx-background-color:  bg-selected-color;");

        mangaContainer.setVgap(10);
        mangaContainer.setHgap(10);
        mangaContainer.setPadding(new Insets(15));
        mangaContainer.setAlignment(Pos.CENTER);
        mangaContainer.setStyle("-fx-background-color: bg-selected-color;");


        VBox.setMargin(containerName, new Insets(15));

        mainContainer.getChildren().addAll(containerName, mangaContainer);
    }

    @Override
    protected void updateItem(Observable<MangasPage> item, boolean empty) {
        super.updateItem(item, empty);
        mangaContainer.getChildren().clear();
        if (item == null){
            setGraphic(null);
            containerName.setText(null);
            return;
        }
        else{
            if(getGraphic() == null)
                setGraphic(mainContainer);
            String name;
            item.compose(FxObservableTransformers.doOnNextFx(mangasPage -> {
                    containerName.setText(mangasPage.sourceName);
                }))
                .flatMap(mangasPage -> {
                    // turn mangas to buttons
                    return Observable.fromIterable(mangasPage.mangas)
                            .map(manga -> {
                                var image = manga.source.fetchImage(manga.thumbnailUrl);
                                var mangaButton = new JFXButton();
                                mangaButton.setStyle("-fx-background-radius: 0;");
                                mangaButton.setGraphic(new ImageView(image));
                                mangaButton.setOnAction(event -> {
                                    NavigationManager.navigate(manga);
                                });
                                return mangaButton;
                            });
                })
                .observeOn(JavaFxScheduler.platform())
                .subscribe(mangaButton -> {
                    mangaContainer.getChildren().add(mangaButton);
                });
            }
        }

    }


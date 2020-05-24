package com.vonkez.ui;

import com.vonkez.model.SourceSearchResult;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.time.Instant;

public class SearchResultCell extends ListCell<SourceSearchResult> {
    VBox mainVbox;
    FlowPane mangaFlowPane;
    Label sourceNameLabel;
    DropShadow dropShadow;
    Disposable subscription;

    public SearchResultCell() {
        mainVbox = new VBox();
        mangaFlowPane = new FlowPane();
        sourceNameLabel = new Label();
        //dropShadow = new DropShadow(35, Color.rgb(22,22,22, 0.5));
        sourceNameLabel.setStyle("-fx-font-size: 45; -fx-text-fill: white;");


        mainVbox.setAlignment(Pos.CENTER_LEFT);
        //mainVbox.setEffect(dropShadow);
        //mainVbox.setStyle("-fx-background-color:  bg-selected-color;");

        mangaFlowPane.getStyleClass().add("search-flowpane");

        mangaFlowPane.setVgap(20);
        mangaFlowPane.setHgap(20);
        mangaFlowPane.setPadding(new Insets(15,0,15,0));
        mangaFlowPane.setAlignment(Pos.CENTER_LEFT);


        VBox.setMargin(sourceNameLabel, new Insets(15, 0, 15, 0));

        mainVbox.getChildren().addAll(sourceNameLabel, mangaFlowPane);
    }

    @Override
    protected void updateItem(SourceSearchResult item, boolean empty) {
        System.out.println("update item" + (empty ? " bos":item.source.getName()));
        super.updateItem(item, empty);
        mangaFlowPane.getChildren().clear();
        if (subscription != null)
            // TODO: cancel requests
            subscription.dispose();
        if (item == null){
            setGraphic(null);
        }
        else{
            if(getGraphic() == null)
                setGraphic(mainVbox);
            // change source name
            sourceNameLabel.setText(item.source.getName());

            long startTime = Instant.now().toEpochMilli();

            subscription = Observable.fromIterable(item.mangas)
                    .subscribeOn(Schedulers.io())
                    .map(manga -> {
                        StackPane singleMangaPane = new StackPane();
                        var mangaButton = new Button();
                        var mangaNameLabel = new Label();
                        var image = manga.fetchThumbnail(manga.thumbnailUrl);
                        mangaButton.setMaxSize(220, 300);
                        var imageView = new ImageView(image);
                        imageView.setFitWidth(220);
                        imageView.setPreserveRatio(true);
                        mangaButton.setGraphic(imageView);


                        mangaButton.setPadding(new Insets(0));
                        mangaButton.setBorder(null);
                        mangaButton.setBackground(null);

                        StackPane.setAlignment(mangaNameLabel, Pos.BOTTOM_CENTER);
                        mangaNameLabel.getStyleClass().add("search-manga-label");
                        mangaNameLabel.setPrefWidth(220);
                        mangaNameLabel.setText(manga.title);
                        mangaNameLabel.setWrapText(true);

                        mangaButton.setOnAction(event -> {
                            NavigationManager.navigate(manga);
                        });

                        singleMangaPane.getChildren().addAll(mangaButton, mangaNameLabel);
                        return singleMangaPane;
                    })
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(
                            singleMangaPane -> mangaFlowPane.getChildren().add(singleMangaPane),
                            throwable -> {
                                var label = new Label("ERROR");
                                label.setStyle("-fx-text-fill: red; -fx-font-size: 20;");
                                mangaFlowPane.getChildren().add(label);
                            });



//            item.mangas.forEach(manga -> {
//                StackPane singleMangaPane = new StackPane();
//                var mangaButton = new Button();
//                var mangaNameLabel = new Label();
//
//                var image = manga.fetchThumbnail(manga.thumbnailUrl);
//                mangaButton.setMaxSize(220,300);
//                var imageView = new ImageView(image);
//                imageView.setFitWidth(220);
//                imageView.setPreserveRatio(true);
//                mangaButton.setGraphic(imageView);
//                mangaButton.setPadding(new Insets(0));
//                mangaButton.setBorder(null);
//                mangaButton.setBackground(null);
//
//                StackPane.setAlignment(mangaNameLabel, Pos.BOTTOM_CENTER);
//                mangaNameLabel.getStyleClass().add("search-manga-label");
//                mangaNameLabel.setPrefWidth(220);
//                mangaNameLabel.setText(manga.title);
//                mangaNameLabel.setWrapText(true);
//                // mangaButton.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(0), new Insets(0))));
//
//                mangaButton.setOnAction(event -> {
//                    NavigationManager.navigate(manga);
//                });
//
//                singleMangaPane.getChildren().addAll(mangaButton, mangaNameLabel);
//                mangaFlowPane.getChildren().add(singleMangaPane);
//
//            });
            System.out.println("SearchResult node creation+thumbnail download in milliseconds(" + item.source.getName() +"): " + (Instant.now().toEpochMilli() - startTime));
            }
        }

    }


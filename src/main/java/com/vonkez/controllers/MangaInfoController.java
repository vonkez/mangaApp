package com.vonkez.controllers;

import com.vonkez.model.Chapter;
import com.vonkez.model.Manga;
import com.vonkez.ui.ChapterListCell;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;


import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;

public class MangaInfoController implements Initializable, BaseController {
    @FXML
    Label title;
    @FXML
    Label author;
    @FXML
    Label artist;
    @FXML
    Label lastChapter;
    @FXML
    Label update;
    @FXML
    Label source;
    @FXML
    Label status;
    @FXML
    Label descripton;
//    @FXML
//    private ImageView thumbnail;

    @FXML
    private BorderPane thumbnailWrapper;

    @FXML
    private StackPane root;
    @FXML
    private StackPane container;
    @FXML
    private ListView chapterList;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox metadataTextVbox;
    @FXML
    private VBox metadataDescVbox;
    @FXML
    private HBox spacer1;
    @FXML
    private HBox buttonHbox;
    @FXML
    private VBox wrapper;


    @Override
    public void initData(Object data) {
        Manga manga = (Manga)data;
        var thumbnail = new ImageView();
        thumbnail.setFitHeight(270);
        thumbnail.getStyleClass().add("manga-thumbnail");

        long startTime = Instant.now().toEpochMilli();

        if(manga.url != null) {
            Observable.just(manga)
                    .subscribeOn(Schedulers.io())
                    .map(m -> manga.source.fetchMangaDetails(m))
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(mangaWithDetails -> {
                        title.setText(mangaWithDetails.title);
                        author.setText(mangaWithDetails.author);
                        artist.setText(mangaWithDetails.artist);
                        lastChapter.setText("??");
                        update.setText("??");
                        source.setText(mangaWithDetails.source.getName());
                        status.setText(mangaWithDetails.status.toString());
                        descripton.setText(mangaWithDetails.description);


                        long endTime = Instant.now().toEpochMilli();
                        System.out.println("mangaInfo after details fetched time in milliseconds: " + (endTime - startTime));


                        Observable.just(mangaWithDetails.thumbnailUrl)
                                .subscribeOn(Schedulers.io())
                                .map(url -> mangaWithDetails.source.fetchImage(url))
                                .observeOn(JavaFxScheduler.platform())
                                .subscribe(image -> {
                                    thumbnail.setImage(image);
                                    thumbnailWrapper.setCenter(thumbnail);
                                    System.out.println("mangaInfo after thumbnail fetched time in milliseconds: " + (endTime - startTime));

                                });
                    });

            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .map(i -> {
                        var a = manga.source.fetchChapterList(manga);
                        return FXCollections.observableArrayList(a);
                    })
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(list -> {
                        list.forEach(chapter -> {
                                    System.out.println(chapter.name);
                                    System.out.println(chapter.url);
                                });
                        chapterList.setItems(list);
                        chapterList.prefHeightProperty().bind(Bindings.size(list).multiply(35));

                    });

        }
        System.out.println("mangaInfo initdata time in milliseconds: " + (Instant.now().toEpochMilli() - startTime));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        long startTime = Instant.now().toEpochMilli();
        ObservableList ls =  FXCollections.observableArrayList();

        for (int i = 0; i < 2; i++) {
            var a = new Chapter();
            a.name = "TestChapter " + i;
            ls.add(a);
        }
        chapterList.setCellFactory(new Callback<ListView<Chapter>, ListCell<Chapter>>() {
            @Override
            public ListCell<Chapter> call(ListView<Chapter> param) {
                return new ChapterListCell();
            }
        });

        chapterList.prefHeightProperty().bind(Bindings.size(ls).multiply(35));
        chapterList.setItems(ls);

        // https://lithi.io/file/9MQ0.png
        // http://localhost:8080/9MQ0.png
        VBox.setMargin(chapterList, new Insets(5));
        HBox.setMargin(metadataTextVbox, new Insets(15));
        HBox.setMargin(metadataDescVbox, new Insets(15));
        VBox.setVgrow(descripton, Priority.ALWAYS);
        VBox.setVgrow(spacer1, Priority.SOMETIMES);
        VBox.setVgrow(buttonHbox, Priority.NEVER);
        VBox.setVgrow(chapterList, Priority.ALWAYS);

        System.out.println("mangaInfo init time in milliseconds: " + (Instant.now().toEpochMilli() - startTime));

        //thumbnail.setImage(new Image("http://localhost:8080/9MQ0.png"));



        //container.getChildren().add(list);
        //container.setPadding(new Insets(24));
        //scrollPane.setContent(container);

        //StackPane.setMargin(button, new Insets(0, 0, 0, 20));

    }
}

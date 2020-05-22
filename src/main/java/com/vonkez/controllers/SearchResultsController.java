package com.vonkez.controllers;

import com.vonkez.model.MangasPage;
import com.vonkez.source.SourceManager;
import com.vonkez.ui.SearchResultCell;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;

public class SearchResultsController implements Initializable, BaseController {
    @FXML
    private ListView mainList;

    @Override
    public void initData(Object data) {
        String query = (String) data;
        long startTime = Instant.now().toEpochMilli();

        ObservableList<MangasPage> searchResults = FXCollections.observableArrayList();
        SourceManager.getSources().forEach(mangaSource -> {
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .map(integer -> mangaSource.fetchSearchManga(1, query))
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(mangasPage -> searchResults.add(mangasPage));
        });
        System.out.println("sourceSearch observable time in milliseconds: " + (Instant.now().toEpochMilli() - startTime));




        long startTime2 = Instant.now().toEpochMilli();

        mainList.setItems(searchResults);

        System.out.println("list set items  time in milliseconds: " + (Instant.now().toEpochMilli() - startTime2));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainList.setCellFactory(new Callback<ListView<MangasPage>, ListCell<MangasPage>>() {
            @Override
            public ListCell<MangasPage> call(ListView<MangasPage> param) {
                return new SearchResultCell();
            }
        });

//        ObservableList<SearchResult> results = FXCollections.observableArrayList();
//        for (int i = 0; i<6; i++){
//            results.add(new SearchResult("MANGADEX").addMangas(LibraryManager.getLibrary()));
//        }
//        mainList.setItems(results);
    }
}

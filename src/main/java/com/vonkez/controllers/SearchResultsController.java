package com.vonkez.controllers;

import com.jfoenix.controls.JFXSpinner;
import com.vonkez.model.Manga;
import com.vonkez.model.MangasPage;
import com.vonkez.model.SourceSearchResult;
import com.vonkez.source.MangaSource;
import com.vonkez.source.SourceManager;
import com.vonkez.ui.NoSelectionModel;
import com.vonkez.ui.SearchResultCell;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.xml.transform.Source;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchResultsController implements Initializable, BaseController {
    @FXML
    private ListView mainList;
    @FXML
    private JFXSpinner pageSpinner;

    private boolean isLoaded;
    private int searchPageLimit = 1;
    private ObservableList<SourceSearchResult> mainListContent = FXCollections.observableArrayList();

    @Override
    public void initData(Object data) {
        String query = (String) data;
        long startTime = Instant.now().toEpochMilli();
        ArrayList<MangaSource> sources = SourceManager.getSources();
        sources.forEach(mangaSource -> {
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .map(i -> searchAllPages(mangaSource, query))
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(
                            searchResult -> {
                                if (!searchResult.mangas.isEmpty())
                                    mainListContent.add(searchResult);
                                    hideLoading();
                            },
                            throwable -> System.out.println("Search error: "+ mangaSource.getName())

                    );
        });

        System.out.println("sourceSearch observable time in milliseconds: " + (Instant.now().toEpochMilli() - startTime));


        long startTime2 = Instant.now().toEpochMilli();

        mainList.setItems(mainListContent);

        System.out.println("list set items  time in milliseconds: " + (Instant.now().toEpochMilli() - startTime2));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainList.setSelectionModel(new NoSelectionModel<SourceSearchResult>());

        mainList.setCellFactory(new Callback<ListView<SourceSearchResult>, ListCell<SourceSearchResult>>() {
            @Override
            public ListCell<SourceSearchResult> call(ListView<SourceSearchResult> param) {
                return new SearchResultCell();
            }
        });

//        ObservableList<SearchResult> results = FXCollections.observableArrayList();
//        for (int i = 0; i<6; i++){
//            results.add(new SearchResult("MANGADEX").addMangas(LibraryManager.getLibrary()));
//        }
//        mainList.setItems(results);
    }

    public void showLoading() {
        mainList.setVisible(false);
        mainList.setManaged(false);
        pageSpinner.setVisible(true);
        pageSpinner.setManaged(true);
    }

    public void hideLoading() {
        if (isLoaded)
            return;
        mainList.setVisible(true);
        mainList.setManaged(true);
        pageSpinner.setVisible(false);
        pageSpinner.setManaged(false);
        isLoaded = true;
    }

    public SourceSearchResult searchAllPages(MangaSource source, String query) {
        System.out.println("Searching " + source.getName());
        int page = 1;
        MangasPage mangasPage;
        SourceSearchResult result = new SourceSearchResult();
        result.source = source;
        do {
            if (page > searchPageLimit)
                break;
            mangasPage = source.fetchSearchManga(page++, query);
            result.mangas.addAll(mangasPage.mangas);
        } while (mangasPage.hasNextPage);

        System.out.println("Search ended " + source.getName() + " (" + result.mangas.size() + " manga found)");
        return result;

    }

}

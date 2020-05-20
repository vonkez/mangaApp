package com.vonkez.controllers;

import com.vonkez.model.MangasPage;
import com.vonkez.source.SourceManager;
import com.vonkez.ui.SearchResultCell;
import io.reactivex.Observable;
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
        ObservableList<Observable<MangasPage>> searchResults = SourceManager.sourceSearch(query);
        long endTime = Instant.now().toEpochMilli();
        System.out.println("sourceSearch  time in milliseconds: " + (endTime - startTime));

        long startTime2 = Instant.now().toEpochMilli();

        mainList.setItems(searchResults);
        long endTime2 = Instant.now().toEpochMilli();
        System.out.println("list set items  time in milliseconds: " + (endTime2 - startTime2));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainList.setCellFactory(new Callback<ListView<Observable<MangasPage>>, ListCell<Observable<MangasPage>>>() {
            @Override
            public ListCell<Observable<MangasPage>> call(ListView<Observable<MangasPage>> param) {
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

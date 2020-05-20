package com.vonkez.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchResult {
    public String sourceName;
    public ObservableList<Manga> mangas;
    public SearchResult(String sourceName) {
        this.sourceName = sourceName;
        this.mangas = FXCollections.observableArrayList();

    }
    public SearchResult addManga(Manga manga){
        mangas.add(manga);
        return this;
    }
    public SearchResult addMangas(ObservableList<Manga> mangas) {
        this.mangas.addAll(mangas);
        return this;
    }

}

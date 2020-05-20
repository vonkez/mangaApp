package com.vonkez.ui;

import com.vonkez.model.Manga;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LibraryManager {
    public static ObservableList<Manga> getLibrary() {

        ObservableList<Manga> mangas = FXCollections.observableArrayList();
        for (int i=0; i<8; i++){
            var a = new Manga();
            a.title = "Manga Title "+ i;
            a.thumbnailUrl = "https://lithi.io/file/9MQ0.png";
            mangas.add(a);

        }
        return mangas;
    }
}

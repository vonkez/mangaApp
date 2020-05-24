package com.vonkez.ui;

import com.vonkez.database.MangaDao;
import com.vonkez.database.MangaDaoSQLite;
import com.vonkez.model.Manga;
import com.vonkez.source.SourceManager;
import io.reactivex.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class LibraryManager {
    static ObservableList<Manga> libraryMangas = FXCollections.observableArrayList();
    static {
        libraryMangas.addAll(MangaDaoSQLite.getInstance().getAllMangas());

    }

    // https://lithi.io/file/9MQ0.png
    // http://localhost:8080/9MQ0.png
    public static ObservableList<Manga> getFakeLibrary() {
        ObservableList<Manga> mangas = FXCollections.observableArrayList();
        for (int i=0; i<28; i++){
            var a = new Manga();
            a.title = "Manga Title "+ i;
            a.thumbnailUrl = "http://localhost:8080/9MQ0.png";
            a.source = SourceManager.getSource("Mangasushi");
            mangas.add(a);
        }
        return mangas;
    }

    public static ObservableList<Manga> getLibraryMangas() {
        return libraryMangas;
    }

    public static void addToLibrary(Manga manga){
        var instance = MangaDaoSQLite.getInstance();
        instance.addManga(manga);
        libraryMangas.add(manga);
    }

    public static void removeFromLibrary(Manga manga){
        MangaDaoSQLite.getInstance().deleteManga(manga);
        libraryMangas.removeIf(m -> m.id == manga.id);
    }

    public static boolean isInLibrary(Manga manga) {
         return libraryMangas.stream().anyMatch(m -> (m.title.equals(manga.title)) && (m.source.getName().equals(manga.source.getName())));
    }

}

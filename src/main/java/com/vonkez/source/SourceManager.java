package com.vonkez.source;

import com.vonkez.model.MangasPage;
import com.vonkez.source.sources.MadaraFactory;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

public class SourceManager {
    private static ArrayList<MangaSource> sources;

    public static void initialize() {
        sources = new ArrayList<MangaSource>();
        sources.addAll(new MadaraFactory().createSources());
    }
    
    public static ArrayList<MangaSource> getSources() {
        return sources;
    }

    public static MangaSource getSource(String sourceName){
        MangaSource result = sources.stream()
                .filter(source -> source.getName().equals(sourceName))
                .findFirst()
                .get();
        return result;
    }


//    public static ObservableList<Observable<MangasPage>> sourceSearch(String s){
//        ObservableList<Observable<MangasPage>> results = FXCollections.observableArrayList();
//        for (MangaSource source: sources) {
//            long startTime = Instant.now().toEpochMilli();
//
//            results.add(
//                    Observable.just(1)
//                            .subscribeOn(Schedulers.io())
//                            .map(integer -> source.fetchSearchManga(1, s))
//            );
//            long endTime = Instant.now().toEpochMilli();
//        }
//        return results;
//    }


}

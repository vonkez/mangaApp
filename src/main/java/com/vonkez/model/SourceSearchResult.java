package com.vonkez.model;

import com.vonkez.source.MangaSource;
import java.util.ArrayList;

public class SourceSearchResult {
    public MangaSource source;
    public ArrayList<Manga> mangas = new ArrayList<>();

    public SourceSearchResult() {
    }
}

package com.vonkez.model;

import java.util.List;

public class MangasPage {
    public String sourceName;
    public List<Manga> mangas;
    public boolean hasNextPage;
    public MangasPage(String sourceName,List<Manga> mangas, boolean hasNextPage) {
        this.sourceName = sourceName;
        this.mangas = mangas;
        this.hasNextPage = hasNextPage;

    }
}

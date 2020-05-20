package com.vonkez.source;

import com.vonkez.model.Chapter;
import com.vonkez.model.Manga;
import com.vonkez.model.MangasPage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.List;

public abstract class MangaSource{
     public abstract Image fetchSourceThumbnail() throws IOException;
     public abstract String getName();
     public abstract MangasPage fetchSearchManga(int page, String query);
     public abstract Manga fetchMangaDetails(Manga manga);
     public abstract Image fetchImage(String url);
     public abstract List<Chapter> fetchChapterList(Manga manga);
}

package com.vonkez.database;

import com.vonkez.model.Manga;
import java.util.List;

public interface MangaDao {
    List<Manga> getAllMangas();
    Manga getManga(String source, String title);
    void addManga(Manga manga);
    void updateManga(Manga manga);
    void deleteManga(Manga manga);
}

//manga db
//    id int
//    title NVARCHAR(100)
//    author NVARCHAR(100)
//    artist NVARCHAR(100)
//    last_chapter float
//    last_update bigint
//    source NVARCHAR(100)
//    status int
//    genres NVARCHAR(250)
//    desc NVARCHAR(500)
//	  url nvarchar
//    thumbnail_url nvarchar(150)
//    thumbnail blob
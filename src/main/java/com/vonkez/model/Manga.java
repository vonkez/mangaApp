package com.vonkez.model;

import com.vonkez.source.MangaSource;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class Manga {
    public int id;
    public String title;
    public String url;
    public String thumbnailUrl;
    public String author;
    public String artist;
    public String description;
    public STATUS status;
    public String genre;
    public MangaSource source;
    public float lastChapter;
    public long lastUpdate;
    public byte[] thumbnail;



    public Manga() {

    }


    public void setUrlWithoutDomain(String url){
        this.url = getUrlWithoutDomain(url);
    }
    private String getUrlWithoutDomain(String orig) {
        try {
            URI uri = new URI(orig);
            var out = uri.getPath();
            if (uri.getQuery() != null) {
                out += "?" + uri.getQuery();
            }
            if (uri.getFragment() != null) {
                out += "#" + uri.getFragment();
            }
            return out;
        } catch (URISyntaxException e) {
            return orig;
        }
    }

    public InputStream getThumbnailStream(){
        return new ByteArrayInputStream(this.thumbnail.clone());
    }

    public Image fetchThumbnail(String url) {
        // TODO: ?
        if (this.thumbnail == null){
            this.thumbnail = source.fetchImage(url);
            return new Image(getThumbnailStream());
        }
        else{
            return new Image(getThumbnailStream());
        }
    }

    public void mergeWith(Manga manga){
        this.id = this.id == 0 ? manga.id: this.id;
        this.title = this.title == null ? manga.title: this.title;
        this.url = this.url == null ? manga.url: this.url;
        this.thumbnailUrl = this.thumbnailUrl == null ? manga.thumbnailUrl: this.thumbnailUrl;
        this.author = this.author == null ? manga.author: this.author;
        this.artist = this.artist == null ? manga.artist: this.artist;
        this.description = this.description == null ? manga.description: this.description;
        this.status = this.status == null ? manga.status: this.status;
        this.genre = this.genre == null ? manga.genre: this.genre;
        this.source = this.source == null ? manga.source: this.source;
        this.lastChapter = this.lastChapter == 0 ? manga.lastChapter: this.lastChapter;
        this.lastUpdate = this.lastUpdate == 0 ? manga.lastUpdate: this.lastUpdate;
        this.thumbnail = this.thumbnail == null ? manga.thumbnail: this.thumbnail;
    }

    public enum STATUS {
        UNKNOWN,
        ONGOING,
        COMPLETED
    }
}

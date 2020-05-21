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

    public enum STATUS {
        UNKNOWN,
        ONGOING,
        COMPLETED
    }
}

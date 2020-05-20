package com.vonkez.model;

import com.vonkez.source.MangaSource;

import java.net.URI;
import java.net.URISyntaxException;

public class Manga {
    public String title;
    public String url;
    public String  thumbnailUrl;
    public String author;
    public String artist;
    public String description;
    public STATUS status;
    public String genre;
    public MangaSource source;

//    public String artist;
//    public String author;
//    public String description;
//    private String genre;
//    public STATUS status;
//    private String thumbnail_url;


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
    public enum STATUS {
        UNKNOWN,
        ONGOING,
        COMPLETED
    }
}

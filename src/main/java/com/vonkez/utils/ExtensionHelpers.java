package com.vonkez.utils;

import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ExtensionHelpers {
    public static Document asJsoup(Response response){
        try {
            return Jsoup.parse(response.body().string(), response.request().url().toString());
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String substringBefore(String s, String target) {
        try {
            return s.substring(0,s.indexOf(target));
        }
        catch (StringIndexOutOfBoundsException e){
            return s;
        }
    }



}

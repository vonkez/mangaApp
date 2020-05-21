package com.vonkez.source.sources;

import com.vonkez.source.MangaSource;
import com.vonkez.source.SourceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class MadaraFactory implements SourceFactory {

    @Override
    public ArrayList<MangaSource> createSources() {

        return new ArrayList<MangaSource>(List.of(
                new Madara("Mangasushi", "https://mangasushi.net", "en"),
                new Madara("NinjaScans", "https://ninjascans.com", "en"),
                //new Madara("ReadManhua", "https://readmanhua.net", "en", new SimpleDateFormat("dd MMM yy", Locale.US)),
                new Madara("IsekaiScan.com", "https://isekaiscan.com", "en")
                )
        );
    }
}

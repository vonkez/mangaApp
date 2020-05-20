package com.vonkez.source.sources;

import com.vonkez.model.Chapter;
import com.vonkez.model.Manga;
import com.vonkez.model.MangasPage;
import com.vonkez.utils.Requests;
import com.vonkez.source.MangaSource;
import javafx.scene.image.Image;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vonkez.utils.ExtensionHelpers.asJsoup;
import static com.vonkez.utils.ExtensionHelpers.substringBefore;

public class Madara extends MangaSource {
    public String name;
    public String baseUrl;
    public String lang;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
    String DEFAULT_USERAGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64)";
    Headers headers = headersBuilder().build();
    private final OkHttpClient client = new OkHttpClient();

    public Madara(String name, String baseUrl, String lang, SimpleDateFormat dateFormat) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.lang = lang;
        this.dateFormat = dateFormat;
    }
    public Madara(String name, String baseUrl, String lang) {
        this(name, baseUrl, lang, new SimpleDateFormat("MMMM dd, yyyy", Locale.US));

    }

    public boolean supportsLatest = true;
    public String popularMangaUrlSelector = "div.post-title a";
    public String popularMangaSelector(){ return "div.page-item-detail"; }
    public String popularMangaNextPageSelector(){ return "body:not(:has(.no-posts))"; }

    public Request sourceThumbnailRequests(){
        return new Requests.GET("http://localhost:8080/9MQ0.png")
                .build();
    }
    public Request imageRequest(String url){
        return new Requests.GET(url)
                .build();
    }

    public Image fetchSourceThumbnail() throws IOException {
        try (Response response = client.newCall(sourceThumbnailRequests()).execute()) {
            if (!response.isSuccessful()) System.out.println("Request failed: " + response);
            return new Image(response.body().byteStream());
        }
        catch (IOException e){
            System.out.println("Request failed: fetch image");
            e.printStackTrace();
        }
        return null;
    }
    public Image fetchImage(String url) {
        try (Response response = client.newCall(imageRequest(url)).execute()) {
            if (!response.isSuccessful()) System.out.println("Request failed: " + response);
            return new Image(response.body().byteStream());
        }
        catch (IOException e){
            System.out.println("Request failed: fetch image"+ url);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    public Request popularMangaRequest(int page) {
        return new Requests.POST(baseUrl + "/wp-admin/admin-ajax.php")
                .body(formBuilder(page, true))
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
    }

    public Manga popularMangaFromElement(Element element) {
        Manga manga = new Manga();
        Element mangaE = element.select(popularMangaUrlSelector).first();
        if (mangaE != null) {
            manga.setUrlWithoutDomain(mangaE.attr("href"));
            manga.title = mangaE.ownText();
        }
        Element imageE =  element.select("img").first();
        if (imageE != null) {
            manga.thumbnailUrl = imageFromElement(imageE);
        }
        manga.source = this;
        return manga;
    }

    private String imageFromElement(Element element) {

        if (element.hasAttr("data-src"))
            return element.attr("abs:data-src");
        else if (element.hasAttr("data-lazy-src"))
            return element.attr("abs:data-lazy-src");
        else if (element.hasAttr("srcset")){
            String x = element.attr("abs:srcset");
            return x.substring(0,x.indexOf(" "));
        }
        else
            return element.attr("abs:src");
    }

    
    MangasPage popularMangaParse(Response response) throws IOException {
        Document document = Jsoup.parse(response.body().string(), response.request().url().toString());
        List<Manga> mangas = document.select(popularMangaSelector()).stream().map(element -> popularMangaFromElement(element)).collect(Collectors.toList());
        boolean hasNextPage = document.select(popularMangaNextPageSelector()).first() != null;
        return new MangasPage(this.name, mangas, hasNextPage);
    }

    public FormBody formBuilder(int page, boolean popular) {
        return new FormBody.Builder()
            .add("action", "madara_load_more")
            .add("page", String.valueOf(page - 1))
            .add("template", "madara-core/content/content-archive")
            .add("vars[orderby]", "meta_value_num")
            .add("vars[paged]", "1")
            .add("vars[posts_per_page]", "20")
            .add("vars[post_type]", "wp-manga")
            .add("vars[post_status]", "publish")
            .add("vars[meta_key]", popular ? "_wp_manga_views" : "_latest_update")
            .add("vars[order]", "desc")
            .add("vars[sidebar]", popular ? "full" : "right")
            .add("vars[manga_archives_item_layout]", "big_thumbnail")
            .build();

    }
    public Headers.Builder headersBuilder()  {
        return new Headers.Builder()
                .add("User-Agent", DEFAULT_USERAGENT);
    }

    public MangasPage fetchSearchManga(int page, String query) {
        try (Response response = client.newCall(searchMangaRequest(page, query)).execute()) {
            if (!response.isSuccessful()) System.out.println("Request failed: " + response);
            return searchMangaParse(response);
        }
        catch (IOException e){
            System.out.println("fetchSearchManga Request failed: "+ query);
            e.printStackTrace();
        }
        return null;


    }

    public Request searchMangaRequest(int page, String query){
        var url = HttpUrl.parse(baseUrl + "/page/"+ page +"/").newBuilder();
        url.addQueryParameter("s", query);
        url.addQueryParameter("post_type", "wp-manga");
        return new Requests.GET(url.build().toString())
                .headers(headers).build();
    }
    public String searchMangaSelector(){ return "div.c-tabs-item__content";}
    public String searchMangaNextPageSelector(){ return "div.nav-previous, nav.navigation-ajax";}

    public MangasPage searchMangaParse(Response response) {
        var document = asJsoup(response);

        Stream<Manga> mangas = document.select(searchMangaSelector()).stream().map(this::searchMangaFromElement);
        var hasNextPage = document.select(searchMangaNextPageSelector()).first() != null;

        return new MangasPage(this.name, mangas.collect(Collectors.toList()), hasNextPage);
    }

    private Manga searchMangaFromElement(Element element) {
        var manga = new Manga();
        Element it = element.select("div.post-title a").first();
        manga.setUrlWithoutDomain(it.attr("href"));
        manga.title = it.ownText();

        it = element.select("img").first();
        manga.thumbnailUrl = imageFromElement(it);
        manga.source = this;
        return manga;
    }

    public Request mangaDetailsRequest(Manga manga) {
        return new Requests.GET(baseUrl + manga.url).
                headers(headers)
                .build();
    }

    public Manga fetchMangaDetails(Manga manga) {
        try (Response response = client.newCall(mangaDetailsRequest(manga)).execute()) {
            if (!response.isSuccessful()) System.out.println("Request failed: " + response);
            return mangaDetailsParse(asJsoup(response));
        } catch (IOException e) {
            System.out.println("mangaDetail Request failed: " + manga.url);
            e.printStackTrace();
        }
        return null;
    }

        public Manga mangaDetailsParse(Document document){
            Manga manga = new Manga();
            var it = document.select("div.post-title h3").first();
            manga.title = it.ownText();
            it =  document.select("div.author-content").first();
            manga.author = it.text();
            it = document.select("div.artist-content").first();
            manga.artist = it.text();
            var its = document.select("div.description-summary div.summary__content");
            if(!(it.select("p").text().isEmpty())) {
                var elements = its.select("p");
                for(Element e: elements) {
                   manga.description += e.text().replace("<br>", "\n");
                }
                // TODO: ?
            }
            else {
                manga.description = its.text();
            }
            it = document.select("div.summary_image img").first();
            manga.thumbnailUrl = imageFromElement(it);
            it = document.select("div.summary-content").last();
            if (it.text() == "Completed")
                manga.status = Manga.STATUS.COMPLETED;
            else if (it.text() == "OnGoing" || it.text() == "Updating")
                manga.status = Manga.STATUS.ONGOING;
            else manga.status = Manga.STATUS.UNKNOWN;
            ArrayList<String> genres = new ArrayList<String>();

            document.select("div.genres-content a").forEach(element -> {
                var genre = element.text();
                genres.add(genre);
            });
            manga.genre = genres.stream().collect(Collectors.joining(", "));
            manga.source = this;
            return manga;
        }

    public String chapterListSelector() { return "li.wp-manga-chapter"; }
    public String chapterUrlSelector = "a";

    public Request chapterListRequest(Manga manga) {
        return new Requests.GET(baseUrl + manga.url).headers(headers).build();
    }

    public List<Chapter> fetchChapterList(Manga manga) {
        // TODO: check licensed
        try (Response response =  client.newCall(chapterListRequest(manga)).execute()) {
            if (!response.isSuccessful()) System.out.println("Request failed: " + response);
            return chapterListParse(response);
        } catch (IOException e) {
            System.out.println("fetchChapterList Request failed: " + manga.url);
            e.printStackTrace();
        }
        return null;
    }

    public List<Chapter>chapterListParse(Response response) {
        var document = asJsoup(response);
        var dataIdSelector = "div#manga-chapters-holder";

        var elements = document.select(chapterListSelector());

        Elements e;
        if (elements.isEmpty() && !(document.select(dataIdSelector).isEmpty() || (document.select(dataIdSelector) == null))){
            e = getXhrChapters(document.select(dataIdSelector).attr("data-id")).select(chapterListSelector());
        }
        else {
            e = elements;
        }
        return e.stream()
                .map(element -> chapterFromElement(element))
                .collect(Collectors.toList());
    }

    public Document getXhrChapters(String mangaId) {
        var xhrHeaders = headersBuilder().add("Content-Type: application/x-www-form-urlencoded; charset=UTF-8").build();
        var body = RequestBody.create(null, "action=manga_get_chapters&manga=$mangaId");

        try (Response response =  client.newCall(new Requests.POST(baseUrl + "/wp-admin/admin-ajax.php").headers(xhrHeaders).body((FormBody) body).build()).execute()) {
            if (!response.isSuccessful()) System.out.println("Request failed: " + response);
            return asJsoup(response);
        } catch (IOException e) {
            System.out.println("xhr Request failed: " );
            e.printStackTrace();
        }
        return null;
    }

    public Chapter chapterFromElement(Element element) {
        Chapter chapter = new Chapter();

        var urlElement = element.select(chapterUrlSelector).first();

        var it = urlElement.attr("abs:href");
        System.out.println("it: " + it);
        var a = substringBefore(it,"?style=paged");
        if (!it.endsWith("?style=list")){
            a += "?style=list";
        }
        chapter.url = a;
        chapter.name= urlElement.text();

        var imgDate = element.select("img").attr("alt");
        if (!imgDate.isBlank()) {
            // TODO:   chapter.date_upload = parseRelativeDate(imgDate)
            chapter.date_upload = "??";
        } else {
            chapter.date_upload = "??";
        }
        return chapter;
    }

}

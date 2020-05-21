package com.vonkez.database;

import com.vonkez.model.Manga;
import com.vonkez.source.SourceManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MangaDaoSQLite implements MangaDao {
    public static MangaDaoSQLite instance;
    private Connection conn;


    private MangaDaoSQLite() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // create manga table
            String query ="CREATE TABLE IF NOT EXISTS "
                    +"manga("
                    +"id integer primary key, "
                    +"title nvarchar, "
                    +"author nvarchar, "
                    +"artist nvarchar, "
                    +"last_chapter float, "
                    +"last_update integer, "
                    +"source nvarchar, "
                    +"status nvarchar, "
                    +"genre nvarchar, "
                    +"description nvarchar, "
                    +"url nvarchar, "
                    +"thumbnail_url nvarchar, "
                    +"thumbnail blob"
                    +")";
            conn.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Manga> getAllMangas() {
        ArrayList<Manga> mangas = new ArrayList<>();
        String query = "SELECT "
                +"id, "
                +"title, "
                +"author, "
                +"artist, "
                +"last_chapter,"
                +"last_update, "
                +"source, "
                +"status, "
                +"genre, "
                +"description, "
                +"url, "
                +"thumbnail_url, "
                +"thumbnail "
                +"FROM manga;";
        try {
            var result = conn.createStatement().executeQuery(query);
            while(result.next()) {
                var manga = new Manga();
                manga.id = result.getInt(1);
                manga.title = result.getString(2);
                manga.author = result.getString(3);
                manga.artist = result.getString(4);
                manga.lastChapter = result.getFloat(5);
                manga.lastUpdate = result.getLong(6);
                manga.source = SourceManager.getSource(result.getString(7));
                manga.status = Manga.STATUS.valueOf(result.getString(8));
                manga.genre = result.getString(9);
                manga.description = result.getString(10);
                manga.url = result.getString(11);
                manga.thumbnailUrl = result.getString(12);
                manga.thumbnail = result.getBytes(13);
                mangas.add(manga);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return mangas;
    }

    @Override
    public Manga getManga(String source, String title) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void addManga(Manga manga) {
        try {
            String query = "INSERT INTO manga("
                    +"title, "
                    +"author, "
                    +"artist, "
                    +"last_chapter, "
                    +"last_update, "
                    +"source, "
                    +"status, "
                    +"genre, "
                    +"description, "
                    +"url, "
                    +"thumbnail_url, "
                    +"thumbnail) "
                    +"VALUES( ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?);";
            var stmt = conn.prepareStatement(query);
            stmt.setString(1, manga.title);
            stmt.setString(2, manga.author);
            stmt.setString(3, manga.artist);
            stmt.setFloat(4, manga.lastChapter);
            stmt.setLong(5, manga.lastUpdate);
            stmt.setString(6, manga.source.getName());
            stmt.setString(7, manga.status.name());
            stmt.setString(8, manga.genre);
            stmt.setString(9, manga.description);
            stmt.setString(10, manga.url);
            stmt.setString(11, manga.thumbnailUrl);
            stmt.setBytes(12, manga.thumbnail == null ? null:  manga.thumbnail.clone());
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateManga(Manga manga) {
        if (manga.id == 0){
            System.out.println("doesn't exists in db");
            return;
        }
        try {
            String query = "UPDATE manga SET "
                    +"title = ?, "
                    +"author = ?, "
                    +"artist = ?, "
                    +"last_chapter = ?, "
                    +"last_update = ?, "
                    +"source = ?, "
                    +"status = ?, "
                    +"genre = ?, "
                    +"description = ?, "
                    +"url = ?, "
                    +"thumbnail_url = ?, "
                    +"thumbnail = ? "
                    +"WHERE id = ?;";
            var stmt = conn.prepareStatement(query);
            stmt.setString(1, manga.title);
            stmt.setString(2, manga.author);
            stmt.setString(3, manga.artist);
            stmt.setFloat(4, manga.lastChapter);
            stmt.setLong(5, manga.lastUpdate);
            stmt.setString(6, manga.source.getName());
            stmt.setString(7, manga.status.name());
            stmt.setString(8, manga.genre);
            stmt.setString(9, manga.description);
            stmt.setString(10, manga.url);
            stmt.setString(11, manga.thumbnailUrl);
            stmt.setBytes(12, manga.thumbnail == null ? null:  manga.thumbnail.clone());
            stmt.setInt(13, manga.id);
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteManga(Manga manga) {
        if (manga.id == 0){
            System.out.println("doesn't exists in db");
            return;
        }
        try {
            String query = "DELETE FROM manga "
                    +"WHERE id = ?;";

            var stmt = conn.prepareStatement(query);
            stmt.setInt(1, manga.id);
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    public static MangaDaoSQLite getInstance(){
        if (instance==null){
            instance = new MangaDaoSQLite();
        }
        return instance;
    }
}

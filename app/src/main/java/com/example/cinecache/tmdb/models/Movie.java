package com.example.cinecache.tmdb.models;
import java.io.Serializable;

public class Movie implements Serializable{
    private int id;
    private String title;
    private String overview;
    private String release_date;
    private String poster_path;

    public Movie() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public String getRelease_date() {
        return release_date;
    }
    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }
    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getPosterFullUrl() {
        if (poster_path == null || poster_path.isEmpty()) {
            return null;
        }
        return "https://image.tmdb.org/t/p/w500" + poster_path;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", release_date='" + release_date + '\'' +
                ", poster_path='" + poster_path + '\'' +
                '}';
    }
}


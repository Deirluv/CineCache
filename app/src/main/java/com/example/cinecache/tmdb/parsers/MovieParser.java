package com.example.cinecache.tmdb.parsers;

import com.example.cinecache.tmdb.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieParser {

    public static List<Movie> parseMovies(String json) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        JSONObject root = new JSONObject(json);
        JSONArray results = root.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject item = results.getJSONObject(i);
            Movie movie = new Movie();
            movie.setId(item.getInt("id"));
            movie.setTitle(item.optString("title", ""));
            movie.setOverview(item.optString("overview", ""));
            movie.setRelease_date(item.optString("release_date", ""));
            movie.setPoster_path(item.optString("poster_path", null));
            movies.add(movie);
        }

        return movies;
    }
}


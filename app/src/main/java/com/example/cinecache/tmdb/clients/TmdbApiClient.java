package com.example.cinecache.tmdb.clients;

import com.example.cinecache.tmdb.models.Movie;
import com.example.cinecache.tmdb.models.SearchParams;
import com.example.cinecache.tmdb.parsers.MovieParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TmdbApiClient {

    private static final String API_URL = "https://api.themoviedb.org/3";
    private final String apiKey;

    public TmdbApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<Movie> searchMovies(SearchParams params) throws IOException, JSONException {
        if (params.getQuery() == null || params.getQuery().isEmpty()) {
            throw new IllegalArgumentException("Query parameter is required");
        }

        StringBuilder urlBuilder = new StringBuilder(API_URL)
                .append("/search/movie")
                .append("?api_key=").append(apiKey)
                .append("&query=").append(URLEncoder.encode(params.getQuery(), "UTF-8"));

        if (params.getYear() != null) {
            urlBuilder.append("&year=").append(params.getYear());
        }

        if (params.getIncludeAdult() != null) {
            urlBuilder.append("&include_adult=").append(params.getIncludeAdult());
        } else {
            urlBuilder.append("&include_adult=false");
        }

        urlBuilder.append("&language=ru-RU&page=1");

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("API error: " + responseCode + " " + connection.getResponseMessage());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }

            return MovieParser.parseMovies(responseBody.toString());
        }
    }
}

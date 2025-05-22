package com.example.cinecache.data.repositories;

import com.example.cinecache.tmdb.models.Movie;
import java.util.List;

public interface IMovieRepository {
    void insert(Movie movie);
    void insertAll(List<Movie> movies);
    List<Movie> getAll();
    Movie getById(int id);
    boolean isMovieSaved(int movieId);
    void deleteById(int id);
    void clear();
}

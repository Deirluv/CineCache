package com.example.cinecache.data.services;

import com.example.cinecache.data.repositories.MovieRepository;
import com.example.cinecache.tmdb.models.Movie;

import java.util.List;

public class MovieService {

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public void saveMovie(Movie movie) {
        repository.insert(movie);
    }

    public void saveMovies(List<Movie> movies) {
        repository.insertAll(movies);
    }

    public List<Movie> getAllSavedMovies() {
        return repository.getAll();
    }

    public Movie getMovieById(int id) {
        return repository.getById(id);
    }

    public boolean isMovieSaved(int movieId) {
        return repository.isMovieSaved(movieId);
    }

    public void deleteMovie(int id) {
        repository.deleteById(id);
    }

    public void clearAll() {
        repository.clear();
    }
}


package com.example.cinecache;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinecache.data.repositories.MovieRepository;
import com.example.cinecache.data.services.MovieService;
import com.example.cinecache.tmdb.models.Movie;

import java.util.List;

public class SavedMoviesActivity extends AppCompatActivity {

    private ListView savedMoviesList;
    private MovieService movieService;
    private TextView noSavedMoviesText;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_movies);

        savedMoviesList = findViewById(R.id.saved_movies_list);
        noSavedMoviesText = findViewById(R.id.no_saved_movies_text);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        MovieRepository repository = new MovieRepository(this);
        movieService = new MovieService(repository);

        loadSavedMovies();
    }

    private void loadSavedMovies() {
        new Thread(() -> {
            try {
                List<Movie> savedMovies = movieService.getAllSavedMovies();

                runOnUiThread(() -> {
                    if(savedMovies.isEmpty()) {
                        noSavedMoviesText.setVisibility(View.VISIBLE);
                        savedMoviesList.setVisibility(View.GONE);
                    }
                    else{
                        noSavedMoviesText.setVisibility(View.GONE);
                        savedMoviesList.setVisibility(View.VISIBLE);
                        MovieListAdapter adapter = new MovieListAdapter(
                                SavedMoviesActivity.this,
                                savedMovies,
                                movieService,
                                true
                        );
                        savedMoviesList.setAdapter(adapter);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error with loading saved movies: " + e.toString(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}

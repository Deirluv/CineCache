package com.example.cinecache;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinecache.data.repositories.MovieRepository;
import com.example.cinecache.data.services.MovieService;
import com.example.cinecache.tmdb.clients.TmdbApiClient;
import com.example.cinecache.tmdb.models.Movie;
import com.example.cinecache.tmdb.models.SearchParams;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText queryInput;
    private EditText yearInput;
    private CheckBox includeAdult;
    private ListView resultList;
    private Button searchButton;
    private ImageButton savedButton;
    private TmdbApiClient apiClient;
    private MovieRepository movieRepository;
    private MovieService movieService;

    private MovieListAdapter adapter;
    private List<Movie> movies = new ArrayList<>();

    private String API_KEY = "ergergergergerg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiClient = new TmdbApiClient(API_KEY);

        movieRepository = new MovieRepository(getApplicationContext());

        movieService = new MovieService(movieRepository);

        queryInput = findViewById(R.id.query_input);
        yearInput = findViewById(R.id.year_input);
        includeAdult = findViewById(R.id.include_adult);
        resultList = findViewById(R.id.result_list);
        searchButton = findViewById(R.id.search_button);
        savedButton = findViewById(R.id.saved_button);

        adapter = new MovieListAdapter(this, movies, movieService, false);
        resultList.setAdapter(adapter);

        searchButton.setOnClickListener(v -> search());
        savedButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SavedMoviesActivity.class);
            startActivity(intent);
        });
    }

    private void search() {
        String query = queryInput.getText().toString();
        Integer year = yearInput.getText().toString().isEmpty() ? null : Integer.parseInt(yearInput.getText().toString());
        boolean include = includeAdult.isChecked();

        new Thread(() -> {
            try {
                List<Movie> newMovies = apiClient.searchMovies(new SearchParams(query, year, include));

                runOnUiThread(() -> {
                    movies.clear();
                    movies.addAll(newMovies);

                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }




}
package com.example.cinecache;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinecache.data.services.MovieService;
import com.example.cinecache.tmdb.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private List<Movie> movies;
    private MovieService service;

    private boolean isSavedList = false;

    public MovieListAdapter(Context context, List<Movie> movies, MovieService service, boolean isSavedList) {
        super(context, R.layout.movie_list_item, movies);
        this.context = context;
        this.movies = movies;
        this.service = service;
        this.isSavedList = isSavedList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_list_item, parent, false);
        }

        Movie movie = movies.get(position);

        TextView titleView = convertView.findViewById(R.id.movie_title);
        TextView overviewView = convertView.findViewById(R.id.movie_overview);
        TextView releaseDateView = convertView.findViewById(R.id.movie_release_date);
        ImageView posterView = convertView.findViewById(R.id.movie_poster);
        Button saveButton = convertView.findViewById(R.id.save_button);

        titleView.setText(movie.getTitle());
        overviewView.setText(movie.getOverview());
        releaseDateView.setText(movie.getRelease_date());

        String posterUrl = movie.getPosterFullUrl();
        if (posterUrl != null) {
            Picasso.get().load(posterUrl).into(posterView);
        } else {
            posterView.setImageResource(R.drawable.ic_placeholder);
        }

        if (isSavedList) {
            saveButton.setText("Unsave");
            saveButton.setOnClickListener(v -> {
                service.deleteMovie(movie.getId());
                movies.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Unsaved", Toast.LENGTH_SHORT).show();
            });
        } else {
            boolean saved = service.isMovieSaved(movie.getId());
            if (saved) {
                saveButton.setText("Unsave");
                saveButton.setOnClickListener(v -> {
                    service.deleteMovie(movie.getId());
                    movies.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Unsaved", Toast.LENGTH_SHORT).show();
                });
            } else {
                saveButton.setText("Save");
                saveButton.setOnClickListener(v -> {
                    service.saveMovie(movie);
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                });
            }
        }

        return convertView;
    }


}


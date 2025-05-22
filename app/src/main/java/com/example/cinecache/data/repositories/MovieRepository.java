package com.example.cinecache.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cinecache.data.db.MovieDatabaseHelper;
import com.example.cinecache.tmdb.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository implements IMovieRepository {

    private final MovieDatabaseHelper dbHelper;

    public MovieRepository(Context context) {
        this.dbHelper = new MovieDatabaseHelper(context);
    }

    @Override
    public void insert(Movie movie) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = toContentValues(movie);
        db.insertWithOnConflict(MovieDatabaseHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    @Override
    public void insertAll(List<Movie> movies) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Movie movie : movies) {
                db.insertWithOnConflict(MovieDatabaseHelper.TABLE_NAME, null, toContentValues(movie), SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public List<Movie> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(MovieDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
        List<Movie> movies = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                movies.add(fromCursor(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movies;
    }

    @Override
    public Movie getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(MovieDatabaseHelper.TABLE_NAME, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);

        Movie movie = null;
        if (cursor.moveToFirst()) {
            movie = fromCursor(cursor);
        }

        cursor.close();
        db.close();
        return movie;
    }

    public boolean isMovieSaved(int movieId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    "movies",
                    new String[] {"id"},
                    "id = ?",
                    new String[] {String.valueOf(movieId)},
                    null, null, null
            );
            boolean exists = (cursor != null && cursor.moveToFirst());
            return exists;
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    @Override
    public void deleteById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(MovieDatabaseHelper.TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void clear() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(MovieDatabaseHelper.TABLE_NAME, null, null);
        db.close();
    }

    private ContentValues toContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put("id", movie.getId());
        values.put("title", movie.getTitle());
        values.put("overview", movie.getOverview());
        values.put("posterPath", movie.getPoster_path());
        values.put("releaseDate", movie.getRelease_date());
        return values;
    }

    private Movie fromCursor(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow("overview")));
        movie.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow("posterPath")));
        movie.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow("releaseDate")));
        return movie;
    }
}

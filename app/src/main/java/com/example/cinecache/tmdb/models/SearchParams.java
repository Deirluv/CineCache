package com.example.cinecache.tmdb.models;

public class SearchParams {
    private String query;
    private Integer year;
    private Boolean includeAdult;

    public SearchParams(String query, Integer year, Boolean includeAdult) {
        this.query = query;
        this.year = year;
        this.includeAdult = includeAdult;
    }

    public String getQuery() {
        return query;
    }
    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getIncludeAdult() {
        return includeAdult;
    }
    public void setIncludeAdult(Boolean includeAdult) {
        this.includeAdult = includeAdult;
    }
}
package com.example.mechanic2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class WebMovies implements Serializable {

    @SerializedName("user_id")
    @Expose
    private Integer user_id;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("movie_url")
    @Expose
    private String movie_url;

    public WebMovies() {
    }

    public WebMovies(Integer movie_offset, Integer movie_size, Integer user_id, Integer id, String movie_preview, String movie_desc, String movie_url) {
        this.user_id = user_id;
        this.id = id;
        this.movie_url = movie_url;
    }


    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public void setMovie_url(String movie_url) {
        this.movie_url = movie_url;
    }

    public String getMovie_url() {
        return movie_url;
    }
}
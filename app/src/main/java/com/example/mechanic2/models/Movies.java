package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class Movies {
  @SerializedName("movie_size")
  @Expose
  private Integer movie_size;
  @SerializedName("movie_url")
  @Expose
  private String movie_url;
  public void setMovie_size(Integer movie_size){
   this.movie_size=movie_size;
  }
  public Integer getMovie_size(){
   return movie_size;
  }
  public void setMovie_url(String movie_url){
   this.movie_url=movie_url;
  }
  public String getMovie_url(){
   return movie_url;
  }
}
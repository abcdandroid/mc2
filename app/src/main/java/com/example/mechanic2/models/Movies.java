package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Movies implements Serializable {
  @SerializedName("movie_offset")
  @Expose
  private Integer movie_offset;
  @SerializedName("movie_size")
  @Expose
  private Integer movie_size;
  @SerializedName("user_id")
  @Expose
  private Integer user_id;
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("movie_preview")
  @Expose
  private String movie_preview;
  @SerializedName("movie_desc")
  @Expose
  private String movie_desc;
  @SerializedName("movie_url")
  @Expose
  private String movie_url;
  public Movies(){
  }
  public Movies(Integer movie_offset,Integer movie_size,Integer user_id,Integer id,String movie_preview,String movie_desc,String movie_url){
   this.movie_offset=movie_offset;
   this.movie_size=movie_size;
   this.user_id=user_id;
   this.id=id;
   this.movie_preview=movie_preview;
   this.movie_desc=movie_desc;
   this.movie_url=movie_url;
  }
  public void setMovie_offset(Integer movie_offset){
   this.movie_offset=movie_offset;
  }
  public Integer getMovie_offset(){
   return movie_offset;
  }
  public void setMovie_size(Integer movie_size){
   this.movie_size=movie_size;
  }
  public Integer getMovie_size(){
   return movie_size;
  }
  public void setUser_id(Integer user_id){
   this.user_id=user_id;
  }
  public Integer getUser_id(){
   return user_id;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
  public void setMovie_preview(String movie_preview){
   this.movie_preview=movie_preview;
  }
  public String getMovie_preview(){
   return movie_preview;
  }
  public void setMovie_desc(String movie_desc){
   this.movie_desc=movie_desc;
  }
  public String getMovie_desc(){
   return movie_desc;
  }
  public void setMovie_url(String movie_url){
   this.movie_url=movie_url;
  }
  public String getMovie_url(){
   return movie_url;
  }
}
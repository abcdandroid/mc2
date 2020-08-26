package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class AdminMedia{
  @SerializedName("media_desc")
  @Expose
  private String media_desc;
  @SerializedName("movie_offset")
  @Expose
  private Integer movie_offset;
  @SerializedName("movie_size")
  @Expose
  private Integer movie_size;
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("state")
  @Expose
  private Integer state;
  @SerializedName("movie_preview")
  @Expose
  private String movie_preview;
  @SerializedName("movie_url")
  @Expose
  private String movie_url;
  public AdminMedia(){
  }
  public AdminMedia(String media_desc,Integer movie_offset,Integer movie_size,Integer id,Integer state,String movie_preview,String movie_url){
   this.media_desc=media_desc;
   this.movie_offset=movie_offset;
   this.movie_size=movie_size;
   this.id=id;
   this.state=state;
   this.movie_preview=movie_preview;
   this.movie_url=movie_url;
  }
  public void setMedia_desc(String media_desc){
   this.media_desc=media_desc;
  }
  public String getMedia_desc(){
   return media_desc;
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
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
  public void setState(Integer state){
   this.state=state;
  }
  public Integer getState(){
   return state;
  }
  public void setMovie_preview(String movie_preview){
   this.movie_preview=movie_preview;
  }
  public String getMovie_preview(){
   return movie_preview;
  }
  public void setMovie_url(String movie_url){
   this.movie_url=movie_url;
  }
  public String getMovie_url(){
   return movie_url;
  }
}
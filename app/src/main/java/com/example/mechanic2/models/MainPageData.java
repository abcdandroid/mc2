package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class MainPageData{
  @SerializedName("visibility")
  @Expose
  private Integer visibility;
  @SerializedName("field")
  @Expose
  private Integer field;
  @SerializedName("view_url")
  @Expose
  private String view_url;
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("place")
  @Expose
  private Integer place;
  @SerializedName("image_title")
  @Expose
  private String image_title;
  @SerializedName("params")
  @Expose
  private Params params;
  @SerializedName("image_desc")
  @Expose
  private String image_desc;
  public MainPageData(){
  }
  public MainPageData(Integer visibility,Integer field,String view_url,Integer id,Integer place,String image_title,Params params,String image_desc){
   this.visibility=visibility;
   this.field=field;
   this.view_url=view_url;
   this.id=id;
   this.place=place;
   this.image_title=image_title;
   this.params=params;
   this.image_desc=image_desc;
  }
  public void setVisibility(Integer visibility){
   this.visibility=visibility;
  }
  public Integer getVisibility(){
   return visibility;
  }
  public void setField(Integer field){
   this.field=field;
  }
  public Integer getField(){
   return field;
  }
  public void setView_url(String view_url){
   this.view_url=view_url;
  }
  public String getView_url(){
   return view_url;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
  public void setPlace(Integer place){
   this.place=place;
  }
  public Integer getPlace(){
   return place;
  }
  public void setImage_title(String image_title){
   this.image_title=image_title;
  }
  public String getImage_title(){
   return image_title;
  }
  public void setParams(Params params){
   this.params=params;
  }
  public Params getParams(){
   return params;
  }
  public void setImage_desc(String image_desc){
   this.image_desc=image_desc;
  }
  public String getImage_desc(){
   return image_desc;
  }
}
package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Awesome Pojo Generator
 * */
public class Mechanic implements Serializable {
  @SerializedName("entrance_id")
  @Expose
  private Integer entrance_id;
  @SerializedName("address")
  @Expose
  private String address;
  @SerializedName("job_ids")
  @Expose
  private String job_ids;
  @SerializedName("region_id")
  @Expose
  private String region_id;
  @SerializedName("about")
  @Expose
  private String about;
  @SerializedName("store_image_3")
  @Expose
  private String store_image_3;
  @SerializedName("store_image_2")
  @Expose
  private String store_image_2;
  @SerializedName("movies")
  @Expose
  private List<Movies> movies;
  @SerializedName("y_location")
  @Expose
  private Double y_location;
  @SerializedName("score")
  @Expose
  private Integer score;
  @SerializedName("store_image_1")
  @Expose
  private String store_image_1;
  @SerializedName("mechanic_image")
  @Expose
  private String mechanic_image;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("store_name")
  @Expose
  private String store_name;
  @SerializedName("phone_number")
  @Expose
  private Integer phone_number;
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("x_location")
  @Expose
  private Double x_location;
  public Mechanic(){
  }
  public Mechanic(Integer entrance_id,String address,String job_ids,String region_id,String about,String store_image_3,String store_image_2,List<Movies> movies,Double y_location,Integer score,String store_image_1,String mechanic_image,String name,String store_name,Integer phone_number,Integer id,Double x_location){
   this.entrance_id=entrance_id;
   this.address=address;
   this.job_ids=job_ids;
   this.region_id=region_id;
   this.about=about;
   this.store_image_3=store_image_3;
   this.store_image_2=store_image_2;
   this.movies=movies;
   this.y_location=y_location;
   this.score=score;
   this.store_image_1=store_image_1;
   this.mechanic_image=mechanic_image;
   this.name=name;
   this.store_name=store_name;
   this.phone_number=phone_number;
   this.id=id;
   this.x_location=x_location;
  }
  public void setEntrance_id(Integer entrance_id){
   this.entrance_id=entrance_id;
  }
  public Integer getEntrance_id(){
   return entrance_id;
  }
  public void setAddress(String address){
   this.address=address;
  }
  public String getAddress(){
   return address;
  }
  public void setJob_ids(String job_ids){
   this.job_ids=job_ids;
  }
  public String getJob_ids(){
   return job_ids;
  }
  public void setRegion_id(String region_id){
   this.region_id=region_id;
  }
  public String getRegion_id(){
   return region_id;
  }
  public void setAbout(String about){
   this.about=about;
  }
  public String getAbout(){
   return about;
  }
  public void setStore_image_3(String store_image_3){
   this.store_image_3=store_image_3;
  }
  public String getStore_image_3(){
   return store_image_3;
  }
  public void setStore_image_2(String store_image_2){
   this.store_image_2=store_image_2;
  }
  public String getStore_image_2(){
   return store_image_2;
  }
  public void setMovies(List<Movies> movies){
   this.movies=movies;
  }
  public List<Movies> getMovies(){
   return movies;
  }
  public void setY_location(Double y_location){
   this.y_location=y_location;
  }
  public Double getY_location(){
   return y_location;
  }
  public void setScore(Integer score){
   this.score=score;
  }
  public Integer getScore(){
   return score;
  }
  public void setStore_image_1(String store_image_1){
   this.store_image_1=store_image_1;
  }
  public String getStore_image_1(){
   return store_image_1;
  }
  public void setMechanic_image(String mechanic_image){
   this.mechanic_image=mechanic_image;
  }
  public String getMechanic_image(){
   return mechanic_image;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setStore_name(String store_name){
   this.store_name=store_name;
  }
  public String getStore_name(){
   return store_name;
  }
  public void setPhone_number(Integer phone_number){
   this.phone_number=phone_number;
  }
  public Integer getPhone_number(){
   return phone_number;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
  public void setX_location(Double x_location){
   this.x_location=x_location;
  }
  public Double getX_location(){
   return x_location;
  }
}
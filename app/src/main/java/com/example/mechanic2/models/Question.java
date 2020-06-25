package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 * */
public class Question implements Serializable {
  @SerializedName("q_entrance_id")
  @Expose
  private Integer q_entrance_id;
  @SerializedName("q_status")
  @Expose
  private Integer q_status;
  @SerializedName("answerCount")
  @Expose
  private Integer answerCount;
  @SerializedName("carName")
  @Expose
  private String carName;
  @SerializedName("q_title")
  @Expose
  private String q_title;
  @SerializedName("q_image_url2")
  @Expose
  private String q_image_url2;
  @SerializedName("q_id")
  @Expose
  private Integer q_id;
  @SerializedName("q_image_url1")
  @Expose
  private String q_image_url1;
  @SerializedName("seen_count")
  @Expose
  private Integer seen_count;
  @SerializedName("q_text")
  @Expose
  private String q_text;
  @SerializedName("carId")
  @Expose
  private Integer carId;
  @SerializedName("q_image_url3")
  @Expose
  private String q_image_url3;
  public Question(){
  }
  public Question(Integer q_entrance_id, Integer q_status, Integer answerCount, String carName, String q_title, String q_image_url2, Integer q_id, String q_image_url1, Integer seen_count, String q_text, Integer carId, String q_image_url3){
   this.q_entrance_id=q_entrance_id;
   this.q_status=q_status;
   this.answerCount=answerCount;
   this.carName=carName;
   this.q_title=q_title;
   this.q_image_url2=q_image_url2;
   this.q_id=q_id;
   this.q_image_url1=q_image_url1;
   this.seen_count=seen_count;
   this.q_text=q_text;
   this.carId=carId;
   this.q_image_url3=q_image_url3;
  }
  public void setQ_entrance_id(Integer q_entrance_id){
   this.q_entrance_id=q_entrance_id;
  }
  public Integer getQ_entrance_id(){
   return q_entrance_id;
  }
  public void setQ_status(Integer q_status){
   this.q_status=q_status;
  }
  public Integer getQ_status(){
   return q_status;
  }
  public void setAnswerCount(Integer answerCount){
   this.answerCount=answerCount;
  }
  public Integer getAnswerCount(){
   return answerCount;
  }
  public void setCarName(String carName){
   this.carName=carName;
  }
  public String getCarName(){
   return carName;
  }
  public void setQ_title(String q_title){
   this.q_title=q_title;
  }
  public String  getQ_title(){
   return q_title;
  }
  public void setQ_image_url2(String q_image_url2){
   this.q_image_url2=q_image_url2;
  }
  public String getQ_image_url2(){
   return q_image_url2;
  }
  public void setQ_id(Integer q_id){
   this.q_id=q_id;
  }
  public Integer getQ_id(){
   return q_id;
  }
  public void setQ_image_url1(String q_image_url1){
   this.q_image_url1=q_image_url1;
  }
  public String getQ_image_url1(){
   return q_image_url1;
  }
  public void setSeen_count(Integer seen_count){
   this.seen_count=seen_count;
  }
  public Integer getSeen_count(){
   return seen_count;
  }
  public void setQ_text(String q_text){
   this.q_text=q_text;
  }
  public String getQ_text(){
   return q_text;
  }
  public void setCarId(Integer carId){
   this.carId=carId;
  }
  public Integer getCarId(){
   return carId;
  }
  public void setQ_image_url3(String q_image_url3){
   this.q_image_url3=q_image_url3;
  }
  public String getQ_image_url3(){
   return q_image_url3;
  }
}
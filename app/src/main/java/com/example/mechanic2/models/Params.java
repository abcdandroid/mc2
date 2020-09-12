package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Params{
  @SerializedName("detail")
  @Expose
  private Detail detail;
  @SerializedName("type")
  @Expose
  private String type;
  public Params(){
  }
  public Params(Detail detail,String type){
   this.detail=detail;
   this.type=type;
  }
  public void setDetail(Detail detail){
   this.detail=detail;
  }
  public Detail getDetail(){
   return detail;
  }
  public void setType(String type){
   this.type=type;
  }
  public String getType(){
   return type;
  }
}
package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 * */
public class Answers implements Serializable     {
  @SerializedName("answer")
  @Expose
  private Answer answer;
  @SerializedName("mechanic")
  @Expose
  private Mechanic mechanic;
  @SerializedName("type")
  @Expose
  private Integer type;
  public Answers(){
  }
  public Answers(Answer answer,Mechanic mechanic,Integer type){
   this.answer=answer;
   this.mechanic=mechanic;
   this.type=type;
  }
  public void setAnswer(Answer answer){
   this.answer=answer;
  }
  public Answer getAnswer(){
   return answer;
  }
  public void setMechanic(Mechanic mechanic){
   this.mechanic=mechanic;
  }
  public Mechanic getMechanic(){
   return mechanic;
  }
  public void setType(Integer type){
   this.type=type;
  }
  public Integer getType(){
   return type;
  }
}
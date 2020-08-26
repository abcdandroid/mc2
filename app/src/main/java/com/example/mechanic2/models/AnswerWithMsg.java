package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class AnswerWithMsg implements Serializable {
  @SerializedName("msg")
  @Expose
  private String msg;
  @SerializedName("answers")
  @Expose
  private List<Answers> answers;
  public AnswerWithMsg(){
  }
  public AnswerWithMsg(String msg,List<Answers> answers){
   this.msg=msg;
   this.answers=answers;
  }
  public void setMsg(String msg){
   this.msg=msg;
  }
  public String getMsg(){
   return msg;
  }
  public void setAnswers(List<Answers> answers){
   this.answers=answers;
  }
  public List<Answers> getAnswers(){
   return answers;
  }
}
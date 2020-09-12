package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QusetionWithMsg implements Serializable {
  @SerializedName("msg")
  @Expose
  private String msg;
  @SerializedName("result")
  @Expose
  private List<Question> result;
  public void setMsg(String msg){
   this.msg=msg;
  }
  public String getMsg(){
   return msg;
  }
  public void setQuestion(List<Question> result){
   this.result=result;
  }
  public List<Question> getQuestion(){
   return result;
  }
}
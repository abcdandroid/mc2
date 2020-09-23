package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MechanicWithMsg implements Serializable {
  @SerializedName("msg")
  @Expose
  private String msg;
  @SerializedName("mechanic")
  @Expose
  private List<Mechanic> mechanic;
  public MechanicWithMsg(){
  }
  public MechanicWithMsg(String msg,List<Mechanic> mechanic){
   this.msg=msg;
   this.mechanic=mechanic;
  }
  public void setMsg(String msg){
   this.msg=msg;
  }
  public String getMsg(){
   return msg;
  }
  public void setMechanic(List<Mechanic> mechanic){
   this.mechanic=mechanic;
  }
  public List<Mechanic> getMechanic(){
   return mechanic;
  }
}
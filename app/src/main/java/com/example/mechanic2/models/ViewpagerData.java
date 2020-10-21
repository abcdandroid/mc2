package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ViewpagerData implements Serializable {
  @SerializedName("changeState")
  @Expose
  private Integer changeState;
  @SerializedName("delayTime")
  @Expose
  private Integer delayTime;
  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("place")
  @Expose
  private Integer place;
  @SerializedName("intervalTime")
  @Expose
  private Integer intervalTime;
  public ViewpagerData(){
  }
  public ViewpagerData(Integer changeState,Integer delayTime,Integer id,Integer place,Integer intervalTime){
   this.changeState=changeState;
   this.delayTime=delayTime;
   this.id=id;
   this.place=place;
   this.intervalTime=intervalTime;
  }
  public void setChangeState(Integer changeState){
   this.changeState=changeState;
  }
  public Integer getChangeState(){
   return changeState;
  }
  public void setDelayTime(Integer delayTime){
   this.delayTime=delayTime;
  }
  public Integer getDelayTime(){
   return delayTime;
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
  public void setIntervalTime(Integer intervalTime){
   this.intervalTime=intervalTime;
  }
  public Integer getIntervalTime(){
   return intervalTime;
  }
}
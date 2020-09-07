package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class MainData{
  @SerializedName("viewpagerData")
  @Expose
  private List<ViewpagerData> viewpagerData;
  @SerializedName("mainPageData")
  @Expose
  private List<MainPageData> mainPageData;
  public MainData(){
  }
  public MainData(List<ViewpagerData> viewpagerData,List<MainPageData> mainPageData){
   this.viewpagerData=viewpagerData;
   this.mainPageData=mainPageData;
  }
  public void setViewpagerData(List<ViewpagerData> viewpagerData){
   this.viewpagerData=viewpagerData;
  }
  public List<ViewpagerData> getViewpagerData(){
   return viewpagerData;
  }
  public void setMainPageData(List<MainPageData> mainPageData){
   this.mainPageData=mainPageData;
  }
  public List<MainPageData> getMainPageData(){
   return mainPageData;
  }
}
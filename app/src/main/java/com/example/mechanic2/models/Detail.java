package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail{
  @SerializedName("country")
  @Expose
  private Country country;
  @SerializedName("goood")
  @Expose
  private Goood goood;
  @SerializedName("car")
  @Expose
  private Car car;
  @SerializedName("isStockActive")
  @Expose
  private Integer isStockActive;
  @SerializedName("warranty")
  @Expose
  private Warranty warranty;
  public Detail(){
  }
  public Detail(Country country,Goood goood,Car car,Integer isStockActive,Warranty warranty){
   this.country=country;
   this.goood=goood;
   this.car=car;
   this.isStockActive=isStockActive;
   this.warranty=warranty;
  }
  public void setCountry(Country country){
   this.country=country;
  }
  public Country getCountry(){
   return country;
  }
  public void setGoood(Goood goood){
   this.goood=goood;
  }
  public Goood getGoood(){
   return goood;
  }
  public void setCar(Car car){
   this.car=car;
  }
  public Car getCar(){
   return car;
  }
  public void setIsStockActive(Integer isStockActive){
   this.isStockActive=isStockActive;
  }
  public Integer getIsStockActive(){
   return isStockActive;
  }
  public void setWarranty(Warranty warranty){
   this.warranty=warranty;
  }
  public Warranty getWarranty(){
   return warranty;
  }
}
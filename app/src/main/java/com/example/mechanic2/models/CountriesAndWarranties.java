package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class CountriesAndWarranties{
  @SerializedName("countries")
  @Expose
  private List<Countries> countries;
  @SerializedName("warrantys")
  @Expose
  private List<Warrantys> warrantys;
  public CountriesAndWarranties(){
  }
  public CountriesAndWarranties(List<Countries> countries,List<Warrantys> warrantys){
   this.countries=countries;
   this.warrantys=warrantys;
  }
  public void setCountries(List<Countries> countries){
   this.countries=countries;
  }
  public List<Countries> getCountries(){
   return countries;
  }
  public void setWarrantys(List<Warrantys> warrantys){
   this.warrantys=warrantys;
  }
  public List<Warrantys> getWarrantys(){
   return warrantys;
  }
}
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
  private List<Country> countries;
  @SerializedName("warrantys")
  @Expose
  private List<Warranty> warranties;
  public CountriesAndWarranties(){
  }
  public CountriesAndWarranties(List<Country> countries, List<Warranty> warranties){
   this.countries=countries;
   this.warranties = warranties;
  }
  public void setCountries(List<Country> countries){
   this.countries=countries;
  }
  public List<Country> getCountries(){
   return countries;
  }
  public void setWarranties(List<Warranty> warranties){
   this.warranties = warranties;
  }
  public List<Warranty> getWarranties(){
   return warranties;
  }
}
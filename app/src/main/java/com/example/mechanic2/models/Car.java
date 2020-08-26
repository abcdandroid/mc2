package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 * */
public class Car implements Serializable {
    public Car(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("id")
  @Expose
  private Integer id;
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
}
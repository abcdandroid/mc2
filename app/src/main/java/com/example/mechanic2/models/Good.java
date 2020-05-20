package com.example.mechanic2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class Good {
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("voice")
    @Expose
    private String voice;
    @SerializedName("phone")
    @Expose
    private Integer phone;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price_time")
    @Expose
    private String price_time;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("suitable_car")
    @Expose
    private String suitable_car;
    @SerializedName("thumbnails")
    @Expose
    private String thumbnails;
    @SerializedName("good_desc")
    @Expose
    private String good_desc;

    @SerializedName("fileSize")
    @Expose
    private int fileSize;

    @SerializedName("visible")
    @Expose
    private boolean visible;



    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPreview() {
        return preview;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getVoice() {
        return voice;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice_time(String price_time) {
        this.price_time = price_time;
    }

    public String getPrice_time() {
        return price_time;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setSuitable_car(String suitable_car) {
        this.suitable_car = suitable_car;
    }

    public String getSuitable_car() {
        return suitable_car;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setGood_desc(String good_desc) {
        this.good_desc = good_desc;
    }

    public String getGood_desc() {
        return good_desc;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
package com.example.mechanic2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Goood implements Serializable {
    @SerializedName("preview")
    @Expose
    private String preview;


    @SerializedName("sentence_2")
    @Expose
    private String sentence_2;
    @SerializedName("sentence_3")
    @Expose
    private String sentence_3;
    @SerializedName("sentence_1")
    @Expose
    private String sentence_1;


    @SerializedName("voice")
    @Expose
    private String voice;
    @SerializedName("price_time")
    @Expose
    private String price_time;
    @SerializedName("good_id")
    @Expose
    private String good_id;
    @SerializedName("suitable_car")
    @Expose
    private String suitable_car;
    @SerializedName("is_stock")
    @Expose
    private Integer is_stock;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("fileSize")
    @Expose
    private Integer fileSize;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("warranty")
    @Expose
    private String warranty;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("thumbnails")
    @Expose
    private String thumbnails;
    @SerializedName("made_by")
    @Expose
    private String made_by;
    @SerializedName("good_desc")
    @Expose
    private String good_desc;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Goood() {
    }

    public Goood(String preview, String sentence_2, String sentence_3, String sentence_1, String voice, String price_time, String good_id, String suitable_car, Integer is_stock, String phone, Integer fileSize, String price, String warranty, String company, Integer id, String thumbnails, String made_by, String good_desc, Integer status) {
        this.preview = preview;
        this.sentence_2 = sentence_2;
        this.sentence_3 = sentence_3;
        this.sentence_1 = sentence_1;
        this.voice = voice;
        this.price_time = price_time;
        this.good_id = good_id;
        this.suitable_car = suitable_car;
        this.is_stock = is_stock;
        this.phone = phone;
        this.fileSize = fileSize;
        this.price = price;
        this.warranty = warranty;
        this.company = company;
        this.id = id;
        this.thumbnails = thumbnails;
        this.made_by = made_by;
        this.good_desc = good_desc;
        this.status = status;
    }



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

    public void setPrice_time(String price_time) {
        this.price_time = price_time;
    }

    public String getPrice_time() {
        return price_time;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public String getGood_id() {
        return good_id;
    }

    public void setSuitable_car(String suitable_car) {
        this.suitable_car = suitable_car;
    }

    public String getSuitable_car() {
        return suitable_car;
    }

    public void setIs_stock(Integer is_stock) {
        this.is_stock = is_stock;
    }

    public Integer getIs_stock() {
        return is_stock;
    }

    public void setPhone(String  phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setPrice(String  price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setMade_by(String made_by) {
        this.made_by = made_by;
    }

    public String getMade_by() {
        return made_by;
    }

    public void setGood_desc(String good_desc) {
        this.good_desc = good_desc;
    }

    public String getGood_desc() {
        return good_desc;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public String getSentence_2() {
        return sentence_2;
    }

    public void setSentence_2(String sentence_2) {
        this.sentence_2 = sentence_2;
    }

    public String getSentence_3() {
        return sentence_3;
    }

    public void setSentence_3(String sentence_3) {
        this.sentence_3 = sentence_3;
    }

    public String getSentence_1() {
        return sentence_1;
    }

    public void setSentence_1(String sentence_1) {
        this.sentence_1 = sentence_1;
    }
}
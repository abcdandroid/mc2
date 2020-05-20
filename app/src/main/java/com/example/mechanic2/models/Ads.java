package com.example.mechanic2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 */
public class Ads implements Serializable {
    @SerializedName("adsImageUrl")
    @Expose
    private String adsImageUrl;
    @SerializedName("adsDesc")
    @Expose
    private String adsDesc;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("goodId")
    @Expose
    private Integer goodId;
    @SerializedName("good")
    @Expose
    private Good good;

    public void setAdsImageUrl(String adsImageUrl) {
        this.adsImageUrl = adsImageUrl;
    }

    public String getAdsImageUrl() {
        return adsImageUrl;
    }

    public void setAdsDesc(String adsDesc) {
        this.adsDesc = adsDesc;
    }

    public String getAdsDesc() {
        return adsDesc;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public Good getGood() {
        return good;
    }
}
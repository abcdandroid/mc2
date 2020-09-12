package com.example.mechanic2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Answer implements Serializable {
    @SerializedName("a_entrance_id")
    @Expose
    private Integer a_entrance_id;
    @SerializedName("a_id")
    @Expose
    private Integer a_id;
    @SerializedName("a_voice_url")
    @Expose
    private String a_voice_url;
    @SerializedName("a_text")
    @Expose
    private String a_text;
    @SerializedName("q_id")
    @Expose
    private Integer q_id;
    @SerializedName("a_status")
    @Expose
    private Integer a_status;
    @SerializedName("fileSize")
    @Expose
    private Integer fileSize;

    public Answer() {
    }

    public Answer(Integer a_entrance_id, Integer fileSize, Integer a_id, String a_voice_url, String a_text, Integer q_id, Integer a_status) {
        this.a_entrance_id = a_entrance_id;
        this.a_id = a_id;
        this.a_voice_url = a_voice_url;
        this.a_text = a_text;
        this.q_id = q_id;
        this.a_status = a_status;
        this.fileSize = fileSize;
    }

    public void setA_entrance_id(Integer a_entrance_id) {
        this.a_entrance_id = a_entrance_id;
    }

    public Integer getA_entrance_id() {
        return a_entrance_id;
    }

    public void setA_id(Integer a_id) {
        this.a_id = a_id;
    }

    public Integer getA_id() {
        return a_id;
    }

    public void setA_voice_url(String a_voice_url) {
        this.a_voice_url = a_voice_url;
    }

    public String getA_voice_url() {
        return a_voice_url;
    }

    public void setA_text(String a_text) {
        this.a_text = a_text;
    }

    public String getA_text() {
        return a_text;
    }

    public void setQ_id(Integer q_id) {
        this.q_id = q_id;
    }

    public Integer getQ_id() {
        return q_id;
    }

    public void setA_status(Integer a_status) {
        this.a_status = a_status;
    }

    public Integer getA_status() {
        return a_status;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getFileSize() {
        return fileSize;
    }
}
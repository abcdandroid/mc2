package com.example.mechanic2.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class AdminMedia{
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("media_desc")
    @Expose
    private String media_desc;
    @SerializedName("fileSize")
    @Expose
    private Integer fileSize;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("url")
    @Expose
    private String url;
    public void setPreview(String preview){
        this.preview=preview;
    }
    public String getPreview(){
        return preview;
    }
    public void setMedia_desc(String media_desc){
        this.media_desc=media_desc;
    }
    public String getMedia_desc(){
        return media_desc;
    }
    public void setFileSize(Integer fileSize){
        this.fileSize=fileSize;
    }
    public Integer getFileSize(){
        return fileSize;
    }
    public void setId(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return id;
    }
    public void setState(Integer state){
        this.state=state;
    }
    public Integer getState(){
        return state;
    }
    public void setUrl(String url){
        this.url=url;
    }
    public String getUrl(){
        return url;
    }
}
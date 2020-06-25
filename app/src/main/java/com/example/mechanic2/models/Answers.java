package com.example.mechanic2.models;

import com.example.mechanic2.app.Application;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Awesome Pojo Generator
 */
public class Answers implements Serializable {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("job_ids")
    @Expose
    private String job_ids;
    @SerializedName("region_id")
    @Expose
    private String region_id;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("store_image_3")
    @Expose
    private String store_image_3;
    @SerializedName("store_image_2")
    @Expose
    private String store_image_2;
    @SerializedName("movies")
    @Expose
    private String movies;
    @SerializedName("y_location")
    @Expose
    private String y_location;
    @SerializedName("a_entrance_id")
    @Expose
    private Integer a_entrance_id;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("store_image_1")
    @Expose
    private String store_image_1;
    @SerializedName("mechanic_image")
    @Expose
    private String mechanic_image;
    @SerializedName("a_id")
    @Expose
    private Integer a_id;
    @SerializedName("a_voice_url")
    @Expose
    private String a_voice_url;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("store_name")
    @Expose
    private String store_name;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;
    @SerializedName("a_text")
    @Expose
    private String a_text;
    @SerializedName("q_id")
    @Expose
    private Integer q_id;
    @SerializedName("x_location")
    @Expose
    private String x_location;
    @SerializedName("a_status")
    @Expose
    private Integer a_status;

    @SerializedName("fileSize")
    @Expose
    private Integer fileSize;

    public Answers() {
    }


    public Answers(String address, String job_ids, String region_id, String about, Integer type, String store_image_3, String store_image_2, String movies, String y_location, Integer a_entrance_id, String score, String store_image_1, String mechanic_image, Integer a_id, String a_voice_url, String name, String store_name, String phone_number, String a_text, Integer q_id, String x_location, Integer a_status, Integer fileSize) {
        this.address = address;
        this.job_ids = job_ids;
        this.region_id = region_id;
        this.about = about;
        this.type = type;
        this.store_image_3 = store_image_3;
        this.store_image_2 = store_image_2;
        this.movies = movies;
        this.y_location = y_location;
        this.a_entrance_id = a_entrance_id;
        this.score = score;
        this.store_image_1 = store_image_1;
        this.mechanic_image = mechanic_image;
        this.a_id = a_id;
        this.a_voice_url = a_voice_url;
        this.name = name;
        this.store_name = store_name;
        this.phone_number = phone_number;
        this.a_text = a_text;
        this.q_id = q_id;
        this.x_location = x_location;
        this.a_status = a_status;
        this.fileSize = fileSize;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setJob_ids(String job_ids) {
        this.job_ids = job_ids;
    }

    public String getJob_ids() {
        return job_ids;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setStore_image_3(String store_image_3) {
        this.store_image_3 = store_image_3;
    }

    public String getStore_image_3() {
        return store_image_3;
    }

    public void setStore_image_2(String store_image_2) {
        this.store_image_2 = store_image_2;
    }

    public String getStore_image_2() {
        return store_image_2;
    }

    public void setMovies(String movies) {
        this.movies = movies;
    }

    public String getMovies() {
        return movies;
    }

    public void setY_location(String y_location) {
        this.y_location = y_location;
    }

    public String getY_location() {
        return y_location;
    }

    public void setA_entrance_id(Integer a_entrance_id) {
        this.a_entrance_id = a_entrance_id;
    }

    public Integer getA_entrance_id() {
        return a_entrance_id;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setStore_image_1(String store_image_1) {
        this.store_image_1 = store_image_1;
    }

    public String getStore_image_1() {
        return store_image_1;
    }

    public void setMechanic_image(String mechanic_image) {
        this.mechanic_image = mechanic_image;
    }

    public String getMechanic_image() {
        return mechanic_image;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhone_number() {
        return phone_number;
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

    public void setX_location(String x_location) {
        this.x_location = x_location;
    }

    public String getX_location() {
        return x_location;
    }

    public void setA_status(Integer a_status) {
        this.a_status = a_status;
    }

    public Integer getA_status() {
        return a_status;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                //requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
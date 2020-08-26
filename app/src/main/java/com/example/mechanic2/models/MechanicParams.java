package com.example.mechanic2.models;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class MechanicParams {
    String route;
    String offset;
    String jogId;
    String regionId;
    String x;
    String y;
    String sortBy;

    public MechanicParams(String route, String offset, String jogId, String regionId, String x, String y, String sortBy) {
        this.route = route;
        this.offset = offset;
        this.jogId = jogId;
        this.regionId = regionId;
        this.x = x;
        this.y = y;
        this.sortBy = sortBy;
    }

    @NotNull
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getJogId() {
        return jogId;
    }

    public void setJogId(String jogId) {
        this.jogId = jogId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}

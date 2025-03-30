package com.example.cursored.model;

import com.google.gson.annotations.SerializedName;

public class CarModel {
    @SerializedName("model_name")
    private String modelName;
    
    @SerializedName("model_make_id")
    private String makeName;

    @SerializedName("model_year")
    private String modelYear;

    @SerializedName("model_image")
    private String imageUrl;

    @SerializedName("model_engine_cc")
    private String engineSize;

    @SerializedName("model_price")
    private String price;

    @SerializedName("model_transmission_type")
    private String transmissionType;

    @SerializedName("model_drive")
    private String driveType;

    @SerializedName("model_fuel_type")
    private String fuelType;

    @SerializedName("model_body_style")
    private String bodyStyle;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(String engineSize) {
        this.engineSize = engineSize;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }
} 
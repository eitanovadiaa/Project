package com.example.cursored.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ModelResponse {
    @SerializedName("Models")
    private List<CarModel> models;

    @SerializedName("Count")
    private int count;

    @SerializedName("Message")
    private String message;

    public List<CarModel> getModels() {
        return models;
    }

    public void setModels(List<CarModel> models) {
        this.models = models;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} 
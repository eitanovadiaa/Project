package com.example.cursored.model;

public class FavoriteCar {
    private String id;
    private String brand;
    private String model;
    private String engineSize;
    private String budget;
    private String userId;  // To associate with Firebase user
    private String year;    // Added year field

    // Default constructor required for Firebase
    public FavoriteCar() {
    }

    public FavoriteCar(String userId, String brand, String model, String engineSize, String budget, String year) {
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.engineSize = engineSize;
        this.budget = budget;
        this.year = year;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(String engineSize) {
        this.engineSize = engineSize;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
} 
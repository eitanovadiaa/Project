package com.example.cursored.model;

public class Car {
    private String brand;
    private String model;
    private int seats;
    private String description;

    public Car(String brand, String model, int seats, String description) {
        this.brand = brand;
        this.model = model;
        this.seats = seats;
        this.description = description;
    }

    // Getters and setters
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getSeats() { return seats; }
    public String getDescription() { return description; }
} 
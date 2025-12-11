package com.example.rideshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class CreateRideRequest {
    @NotBlank(message = "Pickup is required")
    private String pickupLocation;
    @NotBlank(message = "Drop is required")
    private String dropLocation;
    @NotBlank
    private double fare;
    @NotBlank
    private double distanceKm;

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }
    public double getFare() {
        return fare;
    }
    public void setFare(double fare) {
        this.fare = fare;
    }
    public double getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }
}

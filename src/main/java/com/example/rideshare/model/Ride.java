package com.example.rideshare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "rides")
public class Ride {
    @Id
    private String id;
    private String userId;
    private String driverId;
    private String pickupLocation;
    private String dropLocation;
    private RideStatus status;
    private Date createdAt;

    public Ride() {
        this.createdAt = new Date();
        this.status = RideStatus.REQUESTED;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public RideStatus getStatus() {
        return status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

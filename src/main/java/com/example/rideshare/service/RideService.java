package com.example.rideshare.service;

import com.example.rideshare.dto.CreateRideRequest;
import com.example.rideshare.exception.BadRequestException;
import com.example.rideshare.exception.NotFoundException;
import com.example.rideshare.model.Ride;
import com.example.rideshare.model.RideStatus;
import com.example.rideshare.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public Ride createRide(CreateRideRequest request, String userId) {
        Ride ride =  new Ride();
        ride.setUserId(userId);
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        rideRepository.save(ride);
        return ride;
    }

    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus(RideStatus.REQUESTED);
    }

    public Ride acceptRide(String rideId, String driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if(ride.getStatus() == RideStatus.REQUESTED){
            ride.setDriverId(driverId);
            ride.setStatus(RideStatus.ACCEPTED);
            rideRepository.save(ride);
        }else{
            throw new BadRequestException("Ride is already requested");
        }
        return ride;
    }

    public Ride completeRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        if(ride.getStatus() == RideStatus.ACCEPTED){
            ride.setStatus(RideStatus.COMPLETED);
            rideRepository.save(ride);
        }else{
            throw new BadRequestException("Ride is either already completed or in REQUESTED state");
        }
        return ride;
    }

    public List<Ride> getUserRides(String userId) {
        return rideRepository.findByUserId(userId);
    }
}

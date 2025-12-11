package com.example.rideshare.service;

import com.example.rideshare.dto.CreateRideRequest;
import com.example.rideshare.exception.BadRequestException;
import com.example.rideshare.exception.NotFoundException;
import com.example.rideshare.model.Ride;
import com.example.rideshare.model.RideStatus;
import com.example.rideshare.model.User;
import com.example.rideshare.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        ride.setFare(request.getFare());
        ride.setDistanceKm(request.getDistanceKm());
        rideRepository.save(ride);
        return ride;
    }

    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus(RideStatus.REQUESTED);
    }

    @Transactional
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
        User user = (User) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        if(
            !ride.getUserId().equals(user.getId()) &&
            !ride.getDriverId().equals(user.getId())
        ){
            throw new  BadRequestException("You are not allowed to complete this ride");
        }

        if(ride.getStatus() == RideStatus.ACCEPTED){
            ride.setStatus(RideStatus.COMPLETED);
            ride.setCompletedAt(new Date());
            ride.setDuration();
            rideRepository.save(ride);
        }else{
            throw new BadRequestException("Ride is either already completed or in REQUESTED state");
        }
        return ride;
    }

    public Ride cancelRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(
                !ride.getUserId().equals(user.getId()) &&
                        !ride.getDriverId().equals(user.getId())
        ){
            throw new  BadRequestException("You are not allowed to complete this ride");
        }

        if(ride.getStatus() != RideStatus.COMPLETED || ride.getStatus() != RideStatus.CANCELLED){
            ride.setStatus(RideStatus.CANCELLED);
            rideRepository.save(ride);
        }else{
            throw new BadRequestException("Ride is either already completed or in cancelled state");
        }
        return ride;
    }

    public List<Ride> getUserRides(String userId) {

        return rideRepository.findByUserId(userId);
    }
}

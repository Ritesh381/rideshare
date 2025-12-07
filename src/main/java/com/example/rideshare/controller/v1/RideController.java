package com.example.rideshare.controller.v1;

import com.example.rideshare.dto.CreateRideRequest;
import com.example.rideshare.model.Ride;
import com.example.rideshare.model.User;
import com.example.rideshare.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {
    @Autowired
    private RideService rideService;

    // Create ride
    @PostMapping()
    public Ride createRide(@RequestBody CreateRideRequest createRideRequest){
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String userId = user.getId();

        return rideService.createRide(createRideRequest, userId);
    }

    // Complete ride
    @GetMapping("/{id}/complete")
    public Ride completeRide(@PathVariable String id){
        return rideService.completeRide(id);
    }
}

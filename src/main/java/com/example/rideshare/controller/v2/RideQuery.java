package com.example.rideshare.controller.v2;

import com.example.rideshare.model.Ride;
import com.example.rideshare.service.RideQueryServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v2/query")
public class RideQuery {
    @Autowired
    RideQueryServiceV2 rideQueryServiceV2;

    @GetMapping("/searchByLocation")
    public List<Ride> searchRidesByLocation(@RequestParam String location) {
        return rideQueryServiceV2.searchRides(location);
    }

    @GetMapping("/filterByDistance")
    public List<Ride> filterRidesByDistance(@RequestParam Double min, Double max){
        return rideQueryServiceV2.filterByDistance(min, max);
    }

    @GetMapping("/filterByDateRange")
    public List<Ride> filterRidesByDateRange(@RequestParam LocalDate start, LocalDate end){
        return rideQueryServiceV2.filterByDateRange(start, end);
    }

    @GetMapping("/sortByFare")
    public List<Ride> sortByFare(@RequestParam String order) {
        return rideQueryServiceV2.sortByFare(order);
    }
}

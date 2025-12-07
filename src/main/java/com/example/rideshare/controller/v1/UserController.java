package com.example.rideshare.controller.v1;

import com.example.rideshare.exception.BadRequestException;
import com.example.rideshare.model.Ride;
import com.example.rideshare.model.Role;
import com.example.rideshare.model.User;
import com.example.rideshare.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    RideService rideService;

    @GetMapping("/rides")
    public List<Ride> getRequestRides(){
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if(user.getRole() != Role.ROLE_USER){
            throw new BadRequestException("User only endpoint");
        }

        String userId = user.getId();

        return rideService.getUserRides(userId);
    }
}

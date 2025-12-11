package com.example.rideshare.service;

import com.example.rideshare.model.Ride;
import com.example.rideshare.model.RideStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserQueryServiceV2 {
    private final MongoTemplate mongoTemplate;
    public UserQueryServiceV2(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // API 5 : Get rides for user
    public List<Ride> getUserRides(String userId){
        Criteria criteria = Criteria.where("userId").is(userId);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 6 — Get rides for user by status
    public List<Ride> getUserRidesByStatus(String userId, RideStatus status){
        Criteria criteria = Criteria.where("userId").is(userId).and("status").is(status);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }


}

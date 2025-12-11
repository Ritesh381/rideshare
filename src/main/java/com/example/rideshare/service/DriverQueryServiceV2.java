package com.example.rideshare.service;

import com.example.rideshare.model.Ride;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class DriverQueryServiceV2 {
    private final MongoTemplate mongoTemplate;
    public DriverQueryServiceV2(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // API 7 — Driver's active rides
    public List<Ride> getDriverActiveRides(String userId){
        Criteria criteria = Criteria.where("userId").is(userId).and("status").is("ACCEPTED");
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

}

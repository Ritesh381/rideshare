package com.example.rideshare.service;

import com.example.rideshare.model.Ride;
import com.example.rideshare.model.RideStatus;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.print.Doc;
import java.util.List;
import java.util.Map;

public class RideAnalyticsServiceV2 {
    private final MongoTemplate mongoTemplate;
    public RideAnalyticsServiceV2(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // API 10 — Rides per day
    public List<Document> ridesPerDay() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("createdAt").count().as("count"),
                Aggregation.project("count").and("_id").as("date"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "date"))
        );
        return mongoTemplate.aggregate(aggregation, Ride.class, Document.class)
                .getMappedResults();
    }

    // API 11 — Driver summary
    public Map<String, Object> getDriverStats(String driverId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("driverId").is(driverId)),
                Aggregation.group("driverId")
                        .count().as("totalRides")
                        .sum(ConditionalOperators
                                .when(Criteria.where("status").is(RideStatus.COMPLETED))
                                .then(1).otherwise(0))
                        .as("completedRides")
                        .sum(ConditionalOperators
                                .when(Criteria.where("status").is(RideStatus.CANCELLED))
                                .then(1).otherwise(0))
                        .as("cancelledRides")
                        .sum("distanceKm").as("totalDistance")
                        .sum("fare").as("totalFare"),
                Aggregation.project("totalRides", "completedRides",
                        "cancelledRides", "totalDistance", "totalFare")
        );
        List<Document> results = mongoTemplate
                .aggregate(aggregation, "ride", Document.class)
                .getMappedResults();

        if (results.isEmpty()) {
            return Map.of(
                    "totalRides", 0,
                    "completedRides", 0,
                    "cancelledRides", 0,
                    "totalDistance", 0.0,
                    "totalFare", 0.0
            );
        }

        return results.get(0);
    }

    // API 12 — User spending
    public Map<String, Object> getUserSpending(String userId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("userId").is(userId)),
                Aggregation.group("userId")
                        .sum(ConditionalOperators
                                .when(Criteria.where("status").is(RideStatus.COMPLETED))
                                .then(1).otherwise(0))
                        .as("completedRides")
                        .sum("distanceKm").as("totalDistance")
                        .sum("fare").as("totalSpending"),
                Aggregation.project("totalCompletedRides",
                        "totalDistance", "totalSpending")
        );
        List<Document> results = mongoTemplate.aggregate(aggregation, Ride.class, Document.class).getMappedResults();

        if (results.isEmpty()) {
            return Map.of(
                    "totalCompletedRides", 0,
                    "totalDistance", 0,
                    "totalSpending", 0
            );
        }
        return results.get(0);
    }

    // API 13 — Status summary
    public List<Document> getStatusSummary() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("status")
                        .count().as("count"),
                Aggregation.project("count")
                        .and("_id").as("status"),
                Aggregation.sort(Sort.Direction.ASC, "status")
            );
        return mongoTemplate.aggregate(aggregation, "ride", Document.class)
                .getMappedResults();
    }
}

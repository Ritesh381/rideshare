package com.example.rideshare.service;

import com.example.rideshare.model.Ride;
import com.example.rideshare.model.RideStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class RideQueryServiceV2 {
    private final MongoTemplate mongoTemplate;

    public RideQueryServiceV2(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // API 1 : Search rides by pickup OR drop location
    public List<Ride> searchRides(String text) {
        Pattern pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);

        Criteria criteria = new Criteria().orOperator(
                Criteria.where("pickupLocation").regex(pattern),
                Criteria.where("dropLocation").regex(pattern)
        );

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 2 : Filter rides by distance range
    public List<Ride> filterByDistance(Double min, Double max){
        Criteria criteria = Criteria.where("distanceKm").gte(min).lte(max);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 3 : Filter rides between date range
    public List<Ride> filterByDateRange(LocalDate start, LocalDate end){
        Criteria criteria = Criteria.where("createdAt").gte(start).lte(end);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 4 : Sort rides by fare
    public List<Ride> sortByFare(String order) {
        Query query = new Query();
        query.with(Sort.by(order.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "fareAmount"));
        return mongoTemplate.find(query, Ride.class);
    }

    // API 8 — Filter rides by status + keyword
    public List<Ride> filterByStatusAndSearch(RideStatus status, String search) {
        Pattern pattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

        Criteria criteria = new Criteria().andOperator(
                Criteria.where("status").is(status),
                new Criteria().orOperator(
                        Criteria.where("pickupLocation").regex(pattern),
                        Criteria.where("dropLocation").regex(pattern)
                )
        );
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }

    // API 9 — Advanced search + pagination
    public List<Ride> advancedSearch(String search, String status, String sortField,
                                     String order, int page, int size) {

        Pattern pattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);

        Criteria criteria = new Criteria().andOperator(
                Criteria.where("status").is(status),
                new Criteria().orOperator(
                        Criteria.where("pickupLocation").regex(pattern),
                        Criteria.where("dropLocation").regex(pattern)
                )
        );

        Query query = Query.query(criteria);

        Sort.Direction direction =
                "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);

        query.with(PageRequest.of(page, size, sort));

        return mongoTemplate.find(query, Ride.class);
    }

    // API 14 — Rides on specific date
    public List<Ride> ridesOnSpecificDate(LocalDate date) {
        Criteria criteria = Criteria.where("createdAt").lte(date);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Ride.class);
    }
}

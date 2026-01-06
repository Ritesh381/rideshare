package com.example.rideshare.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RideEventConsumer {
    @KafkaListener(topics = "ride-events", groupId = "rideshare-grou[")
    public void consumer(String message) {}
}

package com.example.rideshare.service;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private static final int LIMIT = 5;
    private static final int WINDOW_SECONDS = 60;

    private static class Counter {
        int count;
        long windowStart;
    }

    private final Map<String, Counter> store = new ConcurrentHashMap<>();

    public boolean allowRequest(String key) {
        long now = Instant.now().getEpochSecond();

        Counter counter = store.get(key);

        // First request
        if (counter == null) {
            counter = new Counter();
            counter.count = 1;
            counter.windowStart = now;
            store.put(key, counter);
            return true;
        }

        // Window expired → reset
        if (now - counter.windowStart >= WINDOW_SECONDS) {
            counter.count = 1;
            counter.windowStart = now;
            return true;
        }

        // Within window
        if (counter.count < LIMIT) {
            counter.count++;
            return true;
        }

        // Rate limit exceeded
        return false;
    }
}
package com.example.week4.store;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProcessedEventStore {
    private final Set<String> processed = new HashSet<>();

    public boolean isProcessed(String eventId) {
        return processed.contains(eventId);
    }

    public void markProcessed(String eventId) {
        processed.add(eventId);
    }
}

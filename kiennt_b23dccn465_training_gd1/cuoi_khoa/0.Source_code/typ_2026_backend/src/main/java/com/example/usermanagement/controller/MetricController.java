package com.example.usermanagement.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
@Component
public class MetricController {

    @Autowired
    private MeterRegistry meterRegistry;

    @Value("${spring.application.name:user-management}")
    private String applicationName;
    
    private Counter requestCounter;
    
    @PostConstruct
    public void initMetrics() {
        requestCounter = Counter.builder("api_requests_total")
            .description("Total number of requests to API endpoints")
            .tag("application", applicationName)
            .register(meterRegistry);
    }
    
    public void incrementRequest() {
        requestCounter.increment();
    }
    
    public Timer.Sample startTimer() {
     
        return Timer.start(meterRegistry);
    }
    
   public void stopTimer(Timer.Sample sample, String endpoint) {
 
    
        Timer timer = Timer.builder("api_request_duration")
            .description("Request duration for API endpoints")
            .tag("application", applicationName)
            .tag("endpoint", endpoint)
            .register(meterRegistry);

        sample.stop(timer);
   
}
}
package com.example.week4.dto;
import java.math.BigDecimal;
import java.time.Instant;

public class OrderCreatedEvent {
    private String eventId;
    private Long orderId;
    private String email;
    private BigDecimal total;
    private Instant createdAt;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String eventId, Long orderId, String email, BigDecimal total, Instant createdAt) {
        this.eventId = eventId;
        this.orderId = orderId;
        this.email = email;
        this.total = total;
        this.createdAt = createdAt;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

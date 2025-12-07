package com.example.week4.repository;

import com.example.week4.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<Order,Long> {
}

package com.example.SOA4.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SOA4.entity.Order;
public interface OrderRepository extends JpaRepository<Order,Integer> {}
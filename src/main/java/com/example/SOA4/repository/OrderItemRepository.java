package com.example.SOA4.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SOA4.entity.OrderItem;
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {}
package com.example.SOA4.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import jakarta.persistence.*;
@Entity @Table(name="orders")
public class Order {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(name="customer_name",nullable=false) private String customerName;
 @Column(name="customer_email",nullable=false) private String customerEmail;
 @Column(name="total_amount",precision=10,scale=2,nullable=false) private BigDecimal totalAmount=BigDecimal.ZERO;
 @Column(nullable=false,length=50) private String status="pending";
 @Column(name="created_at",nullable=false) private LocalDateTime createdAt;
 @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
 @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true) private List<OrderItem> items=new ArrayList<>();
 @PrePersist void create(){createdAt=LocalDateTime.now();updatedAt=createdAt;if(status==null)status="pending";}
 @PreUpdate void update(){updatedAt=LocalDateTime.now();}
 public void addItem(OrderItem i){items.add(i);i.setOrder(this);}
 public Integer getId(){return id;} public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
 public String getCustomerEmail(){return customerEmail;} public void setCustomerEmail(String v){customerEmail=v;}
 public BigDecimal getTotalAmount(){return totalAmount;} public void setTotalAmount(BigDecimal v){totalAmount=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
 public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
 public List<OrderItem> getItems(){return items;}
}
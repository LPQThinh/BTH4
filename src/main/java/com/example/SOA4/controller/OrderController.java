package com.example.SOA4.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.SOA4.entity.Order;
import com.example.SOA4.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
@RestController @RequestMapping("/orders")
public class OrderController {
 private final OrderService service;public OrderController(OrderService s){service=s;}
 @GetMapping public List<Order> all(){return service.all();}
 @GetMapping("/{id}") public Order one(@PathVariable Integer id){return service.one(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Order create(@RequestBody OrderService.CreateRequest r,HttpServletRequest req){return service.create(r,req.getHeader("Authorization"));}
 @PutMapping("/{id}") public Order status(@PathVariable Integer id,@RequestBody StatusRequest r){return service.status(id,r.status());}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Integer id){service.delete(id);}
 public record StatusRequest(String status){}
}
package com.example.SOA4.controller;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.SOA4.entity.OrderItem;
import com.example.SOA4.service.OrderItemService;
import jakarta.servlet.http.HttpServletRequest;
@RestController @RequestMapping("/order_items")
public class OrderItemController {
 private final OrderItemService service;public OrderItemController(OrderItemService s){service=s;}
 @GetMapping public List<OrderItem> all(){return service.all();}
 @GetMapping("/{id}") public OrderItem one(@PathVariable Integer id){return service.one(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public OrderItem create(@RequestBody CreateRequest r,HttpServletRequest req){return service.create(r.orderId(),r.productId(),r.quantity(),req.getHeader("Authorization"));}
 @PutMapping("/{id}") public OrderItem update(@PathVariable Integer id,@RequestBody UpdateRequest r){return service.update(id,r.quantity());}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Integer id){service.delete(id);}
 public record CreateRequest(Integer orderId,Integer productId,Integer quantity){}
 public record UpdateRequest(Integer quantity){}
}
package com.example.SOA4.service;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.SOA4.entity.*;
import com.example.SOA4.repository.*;
@Service
public class OrderItemService {
 private final OrderItemRepository items;private final OrderRepository orders;private final ProductClient products;
 public OrderItemService(OrderItemRepository i,OrderRepository o,ProductClient p){items=i;orders=o;products=p;}
 public List<OrderItem> all(){return items.findAll();}
 public OrderItem one(Integer id){return items.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy chi tiết đơn hàng"));}
 public OrderItem create(Integer orderId,Integer productId,Integer qty,String auth){
  if(qty==null||qty<=0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Số lượng phải lớn hơn 0");
  Order o=orders.findById(orderId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy đơn hàng"));
  var p=products.get(productId,auth);if(p.quantity()<qty)throw new ResponseStatusException(HttpStatus.CONFLICT,"Không đủ tồn kho");
  OrderItem i=new OrderItem();i.setOrder(o);i.setProductId(p.id());i.setProductName(p.name());i.setQuantity(qty);i.setUnitPrice(p.price());i.setTotalPrice(p.price().multiply(BigDecimal.valueOf(qty)));
  products.setStock(p,p.quantity()-qty,auth);o.setTotalAmount(o.getTotalAmount().add(i.getTotalPrice()));orders.save(o);return items.save(i);
 }
 public OrderItem update(Integer id,Integer qty){
  if(qty==null||qty<=0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Số lượng phải lớn hơn 0");
  OrderItem i=one(id);BigDecimal total=i.getUnitPrice().multiply(BigDecimal.valueOf(qty));Order o=i.getOrder();
  o.setTotalAmount(o.getTotalAmount().subtract(i.getTotalPrice()).add(total));i.setQuantity(qty);i.setTotalPrice(total);orders.save(o);return items.save(i);
 }
 public void delete(Integer id){OrderItem i=one(id);Order o=i.getOrder();o.setTotalAmount(o.getTotalAmount().subtract(i.getTotalPrice()));orders.save(o);items.delete(i);}
}
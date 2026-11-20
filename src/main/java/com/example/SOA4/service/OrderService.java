package com.example.SOA4.service;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.SOA4.entity.*;
import com.example.SOA4.repository.OrderRepository;
@Service
public class OrderService {
 private final OrderRepository repo; private final ProductClient products;
 public OrderService(OrderRepository r,ProductClient p){repo=r;products=p;}
 public List<Order> all(){return repo.findAll();}
 public Order one(Integer id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy đơn hàng"));}
 @Transactional public Order create(CreateRequest req,String auth){
  if(req.customerName()==null||req.customerEmail()==null||req.items()==null||req.items().isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Thiếu thông tin khách hàng hoặc sản phẩm");
  Order order=new Order();order.setCustomerName(req.customerName());order.setCustomerEmail(req.customerEmail());order.setStatus("pending");
  BigDecimal sum=BigDecimal.ZERO; List<ProductClient.Product> checked=new ArrayList<>();
  for(ItemRequest line:req.items()){
   if(line.quantity()==null||line.quantity()<=0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Số lượng phải lớn hơn 0");
   var p=products.get(line.productId(),auth);
   if(p.quantity()<line.quantity())throw new ResponseStatusException(HttpStatus.CONFLICT,"Không đủ tồn kho cho "+p.name());
   checked.add(p); BigDecimal amount=p.price().multiply(BigDecimal.valueOf(line.quantity()));sum=sum.add(amount);
   OrderItem item=new OrderItem();item.setProductId(p.id());item.setProductName(p.name());item.setQuantity(line.quantity());item.setUnitPrice(p.price());item.setTotalPrice(amount);order.addItem(item);
  }
  order.setTotalAmount(sum);
  for(int i=0;i<checked.size();i++){var p=checked.get(i);products.setStock(p,p.quantity()-req.items().get(i).quantity(),auth);}
  return repo.save(order);
 }
 public Order status(Integer id,String status){
  if(status==null||!List.of("pending","completed","cancelled").contains(status.toLowerCase()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status phải là pending, completed hoặc cancelled");
  Order o=one(id);o.setStatus(status.toLowerCase());return repo.save(o);
 }
 public void delete(Integer id){repo.delete(one(id));}
 public record CreateRequest(String customerName,String customerEmail,List<ItemRequest> items){}
 public record ItemRequest(Integer productId,Integer quantity){}
}
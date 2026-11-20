package com.example.SOA4.service;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.*;
@Component
public class ProductClient {
 @Value("${product.service-url}") private String baseUrl;
 private final HttpClient http=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
 private final ObjectMapper mapper=new ObjectMapper();
 public Product get(Integer id,String auth){
  try{
   var req=HttpRequest.newBuilder().uri(URI.create(baseUrl+"/products/"+id)).header("Authorization",auth).GET().build();
   var res=http.send(req,HttpResponse.BodyHandlers.ofString());
   if(res.statusCode()==404) throw new IllegalArgumentException("Không tìm thấy sản phẩm "+id);
   if(res.statusCode()!=200) throw new IllegalStateException("Product Service trả về "+res.statusCode());
   JsonNode n=mapper.readTree(res.body());
   return new Product(n.path("id").asInt(id),n.path("name").asText(),new BigDecimal(n.path("price").asText("0")),n.path("quantity").asInt());
  }catch(InterruptedException e){Thread.currentThread().interrupt();throw new IllegalStateException("Yêu cầu bị gián đoạn");}
   catch(RuntimeException e){throw e;}catch(Exception e){throw new IllegalStateException("Không thể kết nối Product Service: "+e.getMessage());}
 }
 public void setStock(Product p,int quantity,String auth){
  try{
   String body=mapper.createObjectNode().put("id",p.id()).put("name",p.name()).put("price",p.price()).put("quantity",quantity).toString();
   var req=HttpRequest.newBuilder().uri(URI.create(baseUrl+"/products/"+p.id())).header("Authorization",auth).header("Content-Type","application/json").PUT(HttpRequest.BodyPublishers.ofString(body)).build();
   var res=http.send(req,HttpResponse.BodyHandlers.ofString());
   if(res.statusCode()!=200) throw new IllegalStateException("Không cập nhật được tồn kho sản phẩm "+p.id());
  }catch(InterruptedException e){Thread.currentThread().interrupt();throw new IllegalStateException("Cập nhật tồn kho bị gián đoạn");}
   catch(RuntimeException e){throw e;}catch(Exception e){throw new IllegalStateException(e.getMessage());}
 }
 public record Product(Integer id,String name,BigDecimal price,Integer quantity){}
}
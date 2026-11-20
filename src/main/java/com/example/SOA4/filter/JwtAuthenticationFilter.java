package com.example.SOA4.filter;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 @Value("${jwt.auth-service-url}") private String authUrl;
 private final HttpClient client=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
 @Override protected boolean shouldNotFilter(HttpServletRequest r) {
  String p=r.getRequestURI(); return !p.startsWith("/orders") && !p.startsWith("/order_items");
 }
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain) throws ServletException,IOException {
  String token=req.getHeader("Authorization");
  if(token==null||!token.startsWith("Bearer ")){res.sendError(401,"Missing or invalid JWT token");return;}
  try {
   HttpRequest r=HttpRequest.newBuilder().uri(URI.create(authUrl)).timeout(Duration.ofSeconds(5)).header("Authorization",token).GET().build();
   HttpResponse<Void> a=client.send(r,HttpResponse.BodyHandlers.discarding());
   if(a.statusCode()==200) chain.doFilter(req,res); else res.sendError(401,"Invalid or expired JWT token");
  } catch(InterruptedException e){Thread.currentThread().interrupt();res.sendError(503,"Authentication service unavailable");}
    catch(Exception e){res.sendError(503,"Authentication service unavailable");}
 }
}
# SOA4 - Order Management
- Port: 8083
- Database riêng: orders_db
- JWT service Bài 2: http://localhost:8081/auth
- Product service Bài 3: http://localhost:8082

Chỉnh username/password MySQL trong application.properties. Chạy database/orders_db.sql trong MySQL Workbench (hoặc tạo schema orders_db trước khi chạy ứng dụng). Chạy lần lượt MySQL, Bài 2, Bài 3, rồi Soa4Application.

Tất cả API cần Authorization: Bearer <token>.

POST /orders:
{
  "customerName":"Nguyen Van A",
  "customerEmail":"a@example.com",
  "items":[{"productId":1,"quantity":2}]
}

PUT /orders/1: {"status":"completed"}
POST /order_items: {"orderId":1,"productId":1,"quantity":1}
PUT /order_items/1: {"quantity":3}

Các API còn lại: GET/DELETE /orders, GET/DELETE /orders/{id}, GET/DELETE /order_items, GET/DELETE /order_items/{id}.

Lưu ý: tạo đơn sẽ gọi Product Service GET /products/{id} để kiểm tra tồn kho và PUT /products/{id} để trừ kho. Giao dịch qua hai service không thể được bảo đảm atomic chỉ bằng transaction JPA của orders_db; cần kiểm thử với dữ liệu sản phẩm thực tế.

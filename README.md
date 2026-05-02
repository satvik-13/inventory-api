# 📦 Inventory & Order Management REST API
CHECK OUT THE LIVE APP - https://inventory-api-production-71d8.up.railway.app

A production-grade REST API for retail inventory management built with **Spring Boot 3**, **Hibernate ORM**, **H2/MySQL**, and **Swagger/OpenAPI**.

Designed to mirror real-world e-commerce backend systems like those used at retail SaaS companies.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| ORM | Hibernate / Spring Data JPA |
| Database | H2 (dev) / MySQL (prod) |
| API Docs | Swagger UI / OpenAPI 3 |
| Build | Maven |
| Deploy | Railway |

---

## ✨ Features

- ✅ Full **CRUD** for Products, Categories, and Orders
- ✅ **Stock tracking** with low-stock and out-of-stock alerts
- ✅ **Automatic stock deduction** when an order is placed
- ✅ **Stock restoration** when an order is cancelled
- ✅ **Revenue analytics** from delivered orders
- ✅ **Dashboard endpoint** with full inventory summary
- ✅ **Input validation** with structured error responses
- ✅ **Swagger UI** for interactive public API documentation
- ✅ **H2 Console** to browse the live database in dev mode
- ✅ **Sample data** seeded automatically on startup

---

## ⚙️ Running Locally

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/satvik-13/inventory-api.git
cd inventory-api

# 2. Run the app
./mvnw spring-boot:run

# Windows:
mvnw.cmd spring-boot:run
```

The server starts at **http://localhost:8080**

---

## 🌐 API Endpoints

### Swagger UI (Interactive Docs)
```
http://localhost:8080/swagger-ui.html
```

### H2 Database Console
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:inventorydb
Username: sa  |  Password: (blank)
```

---

### Categories `/api/categories`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/categories` | List all categories |
| GET | `/api/categories/{id}` | Get category by ID |
| POST | `/api/categories` | Create a category |
| PUT | `/api/categories/{id}` | Update a category |
| DELETE | `/api/categories/{id}` | Delete a category |

### Products `/api/products`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/search?name=` | Search by name |
| GET | `/api/products/category/{id}` | Products by category |
| GET | `/api/products/low-stock` | Low stock alert list |
| GET | `/api/products/out-of-stock` | Out of stock products |
| GET | `/api/products/inventory-value` | Total inventory value |
| POST | `/api/products` | Create a product |
| PUT | `/api/products/{id}` | Update a product |
| PATCH | `/api/products/{id}/stock` | Update stock quantity |
| DELETE | `/api/products/{id}` | Delete a product |

### Orders `/api/orders`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/orders` | List all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/status/{status}` | Filter by status |
| GET | `/api/orders/customer?email=` | Orders by customer |
| GET | `/api/orders/revenue` | Total delivered revenue |
| POST | `/api/orders` | Place a new order |
| PATCH | `/api/orders/{id}/status?status=` | Update order status |

### Dashboard `/api/dashboard`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/dashboard` | Full inventory summary |

---

## 📋 Sample Request Bodies

### Create Category
```json
{
  "name": "Apparel",
  "description": "Clothing and fashion items"
}
```

### Create Product
```json
{
  "name": "Classic White Tee",
  "description": "100% cotton unisex t-shirt",
  "price": 299.00,
  "stockQuantity": 150,
  "lowStockThreshold": 20,
  "sku": "APP-001",
  "categoryId": 1
}
```

### Place Order
```json
{
  "customerName": "Rahul Sharma",
  "customerEmail": "rahul@example.com",
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 5, "quantity": 1 }
  ]
}
```

### Update Stock
```json
{
  "quantity": 100,
  "reason": "restock"
}
```

---

## 🗄️ Switching to MySQL (Production)

1. In `application.properties`, comment out the H2 block and uncomment MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventorydb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

2. Create the database:
```sql
CREATE DATABASE inventorydb;
```

3. Change `ddl-auto` to `update` for persistent data:
```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## ☁️ Deploying to Railway

1. Push to GitHub
2. Go to [railway.app](https://railway.app) → New Project → Deploy from GitHub
3. Add a MySQL plugin in Railway dashboard
4. Set environment variables:
```
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:<port>/railway
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```
5. Railway auto-detects Maven and builds the JAR — live in ~2 minutes ✅

---

## 📁 Project Structure

```
src/main/java/com/satvik/inventory/
├── InventoryApiApplication.java   # Entry point
├── SwaggerConfig.java             # OpenAPI configuration
├── DataSeeder.java                # Sample data on startup
├── entity/
│   ├── Category.java
│   ├── Product.java
│   ├── Order.java
│   └── OrderItem.java
├── repository/
│   ├── CategoryRepository.java    # JPQL queries
│   ├── ProductRepository.java
│   └── OrderRepository.java
├── service/
│   ├── CategoryService.java
│   ├── ProductService.java
│   └── OrderService.java
├── controller/
│   ├── CategoryController.java
│   ├── ProductController.java
│   ├── OrderController.java
│   └── DashboardController.java
├── dto/
│   └── Dtos.java                  # All request/response records
└── exception/
    ├── GlobalExceptionHandler.java
    ├── ResourceNotFoundException.java
    └── BadRequestException.java
```

---

## 👤 Author

**Satvik Sarawagi**  
[github.com/satvik-13](https://github.com/satvik-13) | satviksarawagi05@gmail.com

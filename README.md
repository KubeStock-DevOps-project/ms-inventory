# Inventory Service - Production-Ready Microservice

A production-ready Spring Boot microservice for real-time inventory and stock management with PostgreSQL, featuring comprehensive stock tracking, audit logging, and damaged goods management.

## 🚀 Features

- **Real-time Stock Management**: Create, update, adjust, and delete product stock
- **Stock Transactions**: Track all stock movements (IN/OUT/ADJUSTMENT/DAMAGE/RETURN/TRANSFER)
- **Damaged Goods Tracking**: Separate tracking for damaged inventory
- **Audit Logging**: Complete audit trail for all operations
- **Low Stock Alerts**: Query products below reorder levels
- **RESTful API**: Clean REST endpoints with comprehensive validation
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **Health Monitoring**: Spring Boot Actuator with health checks and metrics
- **Database Migration**: Flyway for version-controlled schema management
- **Container Ready**: Multi-stage Dockerfile optimized for production
- **Kubernetes Ready**: Complete K8s deployment manifests included

## 🛠️ Tech Stack

- **Java 21** with Spring Boot 3.5.7
- **Spring Data JPA** for data persistence
- **PostgreSQL 16** database
- **Flyway** for database migrations
- **Lombok** for reducing boilerplate
- **SpringDoc OpenAPI** for API documentation
- **Spring Boot Actuator** for monitoring
- **Docker** & **Kubernetes** for deployment

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.8+
- PostgreSQL 16+ (or use Docker Compose)
- Docker (optional, for containerization)
- Kubernetes cluster (optional, for K8s deployment)

## 🏃 Quick Start

### Using Docker Compose (Recommended)

```bash
docker-compose up --build
```

### Manual Setup

1. **Start PostgreSQL**:
```bash
docker run --name postgres -e POSTGRES_DB=inventory_db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16-alpine
```

2. **Build and Run**:
```bash
./mvnw clean package
java -jar target/ms-inventory-0.0.1-SNAPSHOT.jar
```

## 🌐 API Endpoints

### Product Stock Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/inventory` | Create new product stock |
| GET | `/api/v1/inventory` | Get all product stocks |
| GET | `/api/v1/inventory/{id}` | Get product stock by ID |
| GET | `/api/v1/inventory/sku/{sku}` | Get product stock by SKU |
| PUT | `/api/v1/inventory/{id}` | Update product stock |
| DELETE | `/api/v1/inventory/{id}` | Delete product stock |
| GET | `/api/v1/inventory/low-stock` | Get low stock products |
| GET | `/api/v1/inventory/damaged` | Get products with damaged stock |

### Stock Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/inventory/{id}/adjust` | Adjust stock quantity |
| POST | `/api/v1/inventory/{id}/damage` | Record damaged goods |
| GET | `/api/v1/inventory/{id}/transactions` | Get stock transactions |
| GET | `/api/v1/inventory/{id}/audit-logs` | Get audit logs for product |

### Audit & Monitoring

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/audit` | Get all audit logs |
| GET | `/actuator/health` | Health check endpoint |
| GET | `/actuator/metrics` | Application metrics |
| GET | `/swagger-ui.html` | Interactive API documentation |

## 📝 Sample API Requests

### Create Product Stock
```bash
curl -X POST http://localhost:8080/api/v1/inventory \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU-100",
    "productName": "Wireless Keyboard",
    "quantity": 100,
    "reorderLevel": 20,
    "unitPrice": 49.99,
    "location": "Warehouse-A-Shelf-10"
  }'
```

### Adjust Stock (Stock In)
```bash
curl -X POST http://localhost:8080/api/v1/inventory/1/adjust \
  -H "Content-Type: application/json" \
  -d '{
    "transactionType": "STOCK_IN",
    "quantity": 50,
    "reason": "New shipment received",
    "reference": "PO-2024-001"
  }'
```

### Record Damaged Goods
```bash
curl -X POST http://localhost:8080/api/v1/inventory/1/damage \
  -H "Content-Type: application/json" \
  -d '{
    "damagedQuantity": 5,
    "reason": "Water damage during storage",
    "reference": "DMG-2024-001"
  }'
```

## 🐳 Docker Deployment

### Build Image
```bash
docker build -t inventory-service:latest .
```

### Run Container
```bash
docker run -p 8080:8080 \
  -e DB_HOST=postgres \
  -e DB_PORT=5432 \
  -e DB_NAME=inventory_db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  inventory-service:latest
```

## ☸️ Kubernetes Deployment

```bash
kubectl apply -f k8s/deployment.yaml
```

This will deploy:
- PostgreSQL StatefulSet with persistent storage
- Inventory Service Deployment (2 replicas)
- Services for both components
- Secrets for database credentials

### Access the Service
```bash
kubectl port-forward service/inventory-service 8080:8080
```

## 📊 Database Schema

### Tables
- **product_stock**: Main inventory table with stock quantities
- **stock_transaction**: Records all stock movements
- **audit_log**: Complete audit trail

### Key Features
- Optimistic locking with version control
- Cascading deletes for transactions
- Indexed columns for performance
- Check constraints for data integrity

## 🔍 API Documentation

Access interactive API documentation:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 📈 Monitoring

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

## 🧪 Testing

Sample data is automatically loaded via Flyway migration V4. The database includes 5 sample products for testing.

## 🔧 Configuration

Key environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | PostgreSQL host |
| `DB_PORT` | 5432 | PostgreSQL port |
| `DB_NAME` | inventory_db | Database name |
| `DB_USERNAME` | postgres | Database user |
| `DB_PASSWORD` | postgres | Database password |
| `SERVER_PORT` | 8080 | Application port |

## 📦 Project Structure

```
ms-inventory/
├── src/
│   ├── main/
│   │   ├── java/com/kubestock/inventory/
│   │   │   ├── config/          # OpenAPI configuration
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Custom exceptions & handler
│   │   │   ├── model/           # JPA entities & enums
│   │   │   ├── repository/      # Spring Data repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── db/migration/    # Flyway SQL scripts
│   │       └── application.yml  # Application config
├── k8s/
│   └── deployment.yaml          # Kubernetes manifests
├── docker-compose.yml           # Docker Compose config
├── Dockerfile                   # Multi-stage build
└── pom.xml                      # Maven dependencies
```

## 🎯 Production Considerations

✅ Optimistic locking for concurrency control  
✅ Transaction management for data consistency  
✅ Comprehensive validation and error handling  
✅ Audit logging for compliance  
✅ Health checks and monitoring  
✅ Resource limits in K8s  
✅ Multi-stage Docker build for smaller images  
✅ Non-root container user  
✅ Database connection pooling  
✅ Indexed database columns for performance  

## 📄 License

Apache 2.0

## 🤝 Support

For issues and questions, please open an issue on the repository.

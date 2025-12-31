#!/bin/bash

# Start all services in the correct order
# Usage: ./scripts/start-all.sh

set -e

echo "======================================"
echo "Starting Library Management System"
echo "======================================"
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Navigate to project root
cd "$(dirname "$0")/.."

# Check if services are built
if [ ! -f "infrastructure/service-discovery/target/*.jar" ]; then
    echo -e "${YELLOW}Services not built. Running build first...${NC}"
    ./scripts/build-all.sh
fi

echo -e "${BLUE}Step 1: Starting infrastructure with Docker Compose...${NC}"
docker-compose up -d
echo -e "${GREEN}✓ Infrastructure started${NC}"
echo ""

echo -e "${YELLOW}Waiting for infrastructure to be ready (30 seconds)...${NC}"
sleep 30

echo -e "${BLUE}Step 2: Starting Service Discovery (Eureka)...${NC}"
nohup mvn spring-boot:run -pl infrastructure/service-discovery > logs/service-discovery.log 2>&1 &
echo $! > logs/service-discovery.pid
echo -e "${GREEN}✓ Service Discovery started (PID: $(cat logs/service-discovery.pid))${NC}"
echo ""

echo -e "${YELLOW}Waiting for Service Discovery to be ready (20 seconds)...${NC}"
sleep 20

echo -e "${BLUE}Step 3: Starting Config Server...${NC}"
nohup mvn spring-boot:run -pl infrastructure/config-server > logs/config-server.log 2>&1 &
echo $! > logs/config-server.pid
echo -e "${GREEN}✓ Config Server started (PID: $(cat logs/config-server.pid))${NC}"
echo ""

sleep 10

echo -e "${BLUE}Step 4: Starting microservices...${NC}"

# Auth Service
nohup mvn spring-boot:run -pl services/auth-service > logs/auth-service.log 2>&1 &
echo $! > logs/auth-service.pid
echo -e "${GREEN}✓ Auth Service started (PID: $(cat logs/auth-service.pid))${NC}"

# Book Service
nohup mvn spring-boot:run -pl services/book-service > logs/book-service.log 2>&1 &
echo $! > logs/book-service.pid
echo -e "${GREEN}✓ Book Service started (PID: $(cat logs/book-service.pid))${NC}"

# User Service
nohup mvn spring-boot:run -pl services/user-service > logs/user-service.log 2>&1 &
echo $! > logs/user-service.pid
echo -e "${GREEN}✓ User Service started (PID: $(cat logs/user-service.pid))${NC}"

# Borrowing Service
nohup mvn spring-boot:run -pl services/borrowing-service > logs/borrowing-service.log 2>&1 &
echo $! > logs/borrowing-service.pid
echo -e "${GREEN}✓ Borrowing Service started (PID: $(cat logs/borrowing-service.pid))${NC}"

# Notification Service
nohup mvn spring-boot:run -pl services/notification-service > logs/notification-service.log 2>&1 &
echo $! > logs/notification-service.pid
echo -e "${GREEN}✓ Notification Service started (PID: $(cat logs/notification-service.pid))${NC}"

# Saga Orchestrator Service
nohup mvn spring-boot:run -pl services/saga-orchestrator-service > logs/saga-orchestrator-service.log 2>&1 &
echo $! > logs/saga-orchestrator-service.pid
echo -e "${GREEN}✓ Saga Orchestrator Service started (PID: $(cat logs/saga-orchestrator-service.pid))${NC}"

echo ""
echo -e "${YELLOW}Waiting for services to register (30 seconds)...${NC}"
sleep 30

echo -e "${BLUE}Step 5: Starting API Gateway...${NC}"
nohup mvn spring-boot:run -pl infrastructure/api-gateway > logs/api-gateway.log 2>&1 &
echo $! > logs/api-gateway.pid
echo -e "${GREEN}✓ API Gateway started (PID: $(cat logs/api-gateway.pid))${NC}"
echo ""

echo -e "${GREEN}======================================"
echo "✓ All services started successfully!"
echo "======================================${NC}"
echo ""
echo "Service URLs:"
echo "  - Service Discovery: http://localhost:8761"
echo "  - Config Server: http://localhost:8888"
echo "  - API Gateway: http://localhost:8000"
echo "  - Auth Service: http://localhost:8080"
echo "  - Book Service: http://localhost:8081"
echo "  - User Service: http://localhost:8082"
echo "  - Borrowing Service: http://localhost:8083"
echo "  - Notification Service: http://localhost:8084"
echo "  - Saga Orchestrator: http://localhost:8085"
echo ""
echo "Monitoring:"
echo "  - Prometheus: http://localhost:9090"
echo "  - Grafana: http://localhost:3000 (admin/admin)"
echo "  - Jaeger: http://localhost:16686"
echo "  - Kibana: http://localhost:5601"
echo ""
echo "Logs are available in the logs/ directory"
echo ""

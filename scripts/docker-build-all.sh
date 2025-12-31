#!/bin/bash

# Build Docker images for all services
# Usage: ./scripts/docker-build-all.sh

set -e

echo "======================================"
echo "Building Docker Images"
echo "======================================"
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# Navigate to project root
cd "$(dirname "$0")/.."

# Build common libraries first
echo -e "${BLUE}Building common libraries...${NC}"
mvn clean install -pl common/common-dto,common/common-exception,common/common-util,common/common-security,common/common-event -am
echo -e "${GREEN}✓ Common libraries built${NC}"
echo ""

# Infrastructure services
echo -e "${BLUE}Building Docker image for Service Discovery...${NC}"
docker build -t library/service-discovery:latest -f infrastructure/service-discovery/Dockerfile .
echo -e "${GREEN}✓ Service Discovery image built${NC}"

echo -e "${BLUE}Building Docker image for Config Server...${NC}"
docker build -t library/config-server:latest -f infrastructure/config-server/Dockerfile .
echo -e "${GREEN}✓ Config Server image built${NC}"

echo -e "${BLUE}Building Docker image for API Gateway...${NC}"
docker build -t library/api-gateway:latest -f infrastructure/api-gateway/Dockerfile .
echo -e "${GREEN}✓ API Gateway image built${NC}"
echo ""

# Microservices
SERVICES=("auth-service" "book-service" "user-service" "borrowing-service" "notification-service" "saga-orchestrator-service")

for service in "${SERVICES[@]}"; do
    echo -e "${BLUE}Building Docker image for $service...${NC}"
    docker build -t library/$service:latest -f services/$service/Dockerfile .
    echo -e "${GREEN}✓ $service image built${NC}"
done

echo ""
echo -e "${GREEN}======================================"
echo "✓ All Docker images built successfully!"
echo "======================================${NC}"
echo ""
echo "Built images:"
docker images | grep "library/"
echo ""

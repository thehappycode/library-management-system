#!/bin/bash

# Build all common libraries and services
# Usage: ./scripts/build-all.sh

set -e

echo "======================================"
echo "Building Library Management System"
echo "======================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Navigate to project root
cd "$(dirname "$0")/.."

echo -e "${BLUE}Step 1: Building common libraries...${NC}"
mvn clean install -pl common/common-dto,common/common-exception,common/common-util,common/common-security,common/common-event -am
echo -e "${GREEN}✓ Common libraries built successfully${NC}"
echo ""

echo -e "${BLUE}Step 2: Building microservices...${NC}"
mvn clean package -pl services/auth-service,services/book-service,services/user-service,services/borrowing-service,services/notification-service,services/saga-orchestrator-service -am -DskipTests
echo -e "${GREEN}✓ Microservices built successfully${NC}"
echo ""

echo -e "${BLUE}Step 3: Building infrastructure services...${NC}"
mvn clean package -pl infrastructure/api-gateway,infrastructure/service-discovery,infrastructure/config-server -am -DskipTests
echo -e "${GREEN}✓ Infrastructure services built successfully${NC}"
echo ""

echo -e "${GREEN}======================================"
echo "✓ Build completed successfully!"
echo "======================================${NC}"
echo ""
echo "Next steps:"
echo "  1. Start infrastructure: docker-compose up -d"
echo "  2. Start services: ./scripts/start-all.sh"
echo ""

#!/bin/bash

# Run tests for all services
# Usage: ./scripts/test-all.sh

set -e

echo "======================================"
echo "Running Tests"
echo "======================================"
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

# Navigate to project root
cd "$(dirname "$0")/.."

# Track failures
FAILED_TESTS=()

echo -e "${BLUE}Testing common libraries...${NC}"
if mvn test -pl common/common-dto,common/common-exception,common/common-util,common/common-security,common/common-event; then
    echo -e "${GREEN}✓ Common libraries tests passed${NC}"
else
    echo -e "${RED}✗ Common libraries tests failed${NC}"
    FAILED_TESTS+=("Common Libraries")
fi
echo ""

echo -e "${BLUE}Testing microservices...${NC}"
for service in auth-service book-service user-service borrowing-service notification-service saga-orchestrator-service; do
    echo -e "${BLUE}Testing $service...${NC}"
    if mvn test -pl services/$service; then
        echo -e "${GREEN}✓ $service tests passed${NC}"
    else
        echo -e "${RED}✗ $service tests failed${NC}"
        FAILED_TESTS+=("$service")
    fi
    echo ""
done

echo -e "${BLUE}Testing infrastructure services...${NC}"
for service in api-gateway service-discovery config-server; do
    echo -e "${BLUE}Testing $service...${NC}"
    if mvn test -pl infrastructure/$service; then
        echo -e "${GREEN}✓ $service tests passed${NC}"
    else
        echo -e "${RED}✗ $service tests failed${NC}"
        FAILED_TESTS+=("$service")
    fi
    echo ""
done

echo "======================================"
if [ ${#FAILED_TESTS[@]} -eq 0 ]; then
    echo -e "${GREEN}✓ All tests passed successfully!${NC}"
    echo "======================================"
    exit 0
else
    echo -e "${RED}✗ Some tests failed:${NC}"
    for test in "${FAILED_TESTS[@]}"; do
        echo -e "${RED}  - $test${NC}"
    done
    echo "======================================"
    exit 1
fi

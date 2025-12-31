#!/bin/bash

# Stop all running services
# Usage: ./scripts/stop-all.sh

set -e

echo "======================================"
echo "Stopping Library Management System"
echo "======================================"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Navigate to project root
cd "$(dirname "$0")/.."

echo "Stopping services..."

# Function to stop service by PID file
stop_service() {
    local service_name=$1
    local pid_file="logs/${service_name}.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if ps -p $pid > /dev/null 2>&1; then
            echo "Stopping $service_name (PID: $pid)..."
            kill $pid
            rm "$pid_file"
            echo -e "${GREEN}✓ $service_name stopped${NC}"
        else
            echo -e "${RED}✗ $service_name not running (removing stale PID file)${NC}"
            rm "$pid_file"
        fi
    else
        echo "$service_name: No PID file found"
    fi
}

# Stop all services
stop_service "api-gateway"
stop_service "saga-orchestrator-service"
stop_service "notification-service"
stop_service "borrowing-service"
stop_service "user-service"
stop_service "book-service"
stop_service "auth-service"
stop_service "config-server"
stop_service "service-discovery"

echo ""
echo "Stopping Docker containers..."
docker-compose down
echo -e "${GREEN}✓ Docker containers stopped${NC}"
echo ""

echo -e "${GREEN}======================================"
echo "✓ All services stopped successfully!"
echo "======================================${NC}"
echo ""

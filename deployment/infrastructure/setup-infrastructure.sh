#!/bin/bash

# Infrastructure Setup Script for Ticket Management System
# This script sets up and manages the infrastructure services
#
# Usage:
# ./setup-infrastructure.sh start    - Start all services
# ./setup-infrastructure.sh stop     - Stop all services
# ./setup-infrastructure.sh restart  - Restart all services
# ./setup-infrastructure.sh cleanup  - Stop and remove all containers and volumes
# ./setup-infrastructure.sh status   - Show status of all services
# ./setup-infrastructure.sh logs     - Show logs of all services

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

# Docker compose file path
DOCKER_COMPOSE_FILE="docker-compose.yml"

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_header() {
    echo ""
    echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║${WHITE}$(printf "%58s" "$1")${BLUE}║${NC}"
    echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
    echo ""
}

print_service_header() {
    echo -e "${PURPLE}┌─────────────────────────────────────────────────────────────┐${NC}"
    echo -e "${PURPLE}│${WHITE} $1${PURPLE}│${NC}"
    echo -e "${PURPLE}└─────────────────────────────────────────────────────────────┘${NC}"
}

# Function to check if docker and docker-compose are installed
check_prerequisites() {
    print_status "Checking prerequisites..."

    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi

    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi

    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        print_error "Docker Compose file not found: $DOCKER_COMPOSE_FILE"
        exit 1
    fi

    print_success "All prerequisites met!"
}

# Function to show service URLs
show_service_urls() {
    print_service_header "📋 Service URLs"
    echo -e "${CYAN}PostgreSQL:${NC}     localhost:5433"
    echo -e "${CYAN}pgAdmin:${NC}        http://localhost:5050 (admin@admin.com / admin)"
    echo -e "${CYAN}Redis:${NC}          localhost:6379"
    echo -e "${CYAN}Zookeeper:${NC}      localhost:2181"
    echo -e "${CYAN}Kafka:${NC}          localhost:9092"
    echo -e "${CYAN}Kafka UI:${NC}       http://localhost:8080"
    echo -e "${CYAN}Zipkin:${NC}         http://localhost:9411"
    echo ""
}

# Function to check service health
check_service_health() {
    local service=$1
    local max_attempts=30
    local attempt=1

    print_status "Checking health of $service..."

    while [ $attempt -le $max_attempts ]; do
        if docker-compose exec -T $service echo "Health check" &> /dev/null; then
            print_success "$service is healthy! ✅"
            return 0
        fi

        echo -ne "${YELLOW}[WAITING]${NC} $service health check... attempt $attempt/$max_attempts\r"
        sleep 2
        ((attempt++))
    done

    echo ""
    print_warning "$service may not be fully ready yet ⚠️"
    return 1
}

# Function to start services
start_services() {
    print_header "🚀 Starting Infrastructure Services"

    check_prerequisites

    print_status "Starting services with Docker Compose..."
    docker-compose up -d

    print_success "All services started successfully!"

    # Wait a bit for services to initialize
    print_status "Waiting for services to initialize..."
    sleep 10

    # Check health of critical services
    print_service_header "🔍 Health Check Status"
    check_service_health "postgres" || true
    check_service_health "redis" || true
    check_service_health "zookeeper" || true
    check_service_health "kafka" || true
    check_service_health "zipkin" || true

    show_service_urls
    print_success "Infrastructure setup completed! 🎉"
}

# Function to stop services
stop_services() {
    print_header "🛑 Stopping Infrastructure Services"

    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        print_error "Docker Compose file not found: $DOCKER_COMPOSE_FILE"
        exit 1
    fi

    print_status "Stopping all services..."
    docker-compose stop

    print_success "All services stopped successfully! ✅"
}

# Function to restart services
restart_services() {
    print_header "🔄 Restarting Infrastructure Services"
    stop_services
    sleep 3
    start_services
}

# Function to cleanup (stop and remove containers, networks, volumes)
cleanup_services() {
    print_header "🧹 Cleaning Up Infrastructure"

    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        print_error "Docker Compose file not found: $DOCKER_COMPOSE_FILE"
        exit 1
    fi

    print_warning "This will remove all containers, networks, and volumes!"
    read -p "Are you sure you want to continue? (y/N): " -n 1 -r
    echo

    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_status "Stopping and removing all services..."
        docker-compose down -v --remove-orphans

        print_status "Removing unused networks..."
        docker network prune -f

        print_status "Removing unused volumes..."
        docker volume prune -f

        print_success "Cleanup completed successfully! ✅"
    else
        print_warning "Cleanup cancelled."
    fi
}

# Function to show service status
show_status() {
    print_header "📊 Infrastructure Services Status"

    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        print_error "Docker Compose file not found: $DOCKER_COMPOSE_FILE"
        exit 1
    fi

    print_service_header "🐳 Container Status"
    docker-compose ps

    echo ""
    print_service_header "💾 Volume Usage"
    docker system df

    show_service_urls
}

# Function to show logs
show_logs() {
    print_header "📜 Infrastructure Services Logs"

    if [ ! -f "$DOCKER_COMPOSE_FILE" ]; then
        print_error "Docker Compose file not found: $DOCKER_COMPOSE_FILE"
        exit 1
    fi

    print_status "Showing logs for all services (press Ctrl+C to exit)..."
    docker-compose logs -f --tail=50
}

# Function to show help
show_help() {
    print_header "📖 Infrastructure Setup Script Help"

    echo -e "${WHITE}Usage:${NC}"
    echo -e "  ./setup-infrastructure.sh ${GREEN}start${NC}    - Start all infrastructure services"
    echo -e "  ./setup-infrastructure.sh ${RED}stop${NC}     - Stop all infrastructure services"
    echo -e "  ./setup-infrastructure.sh ${YELLOW}restart${NC}  - Restart all infrastructure services"
    echo -e "  ./setup-infrastructure.sh ${PURPLE}cleanup${NC}  - Stop and remove all containers and volumes"
    echo -e "  ./setup-infrastructure.sh ${BLUE}status${NC}   - Show status of all services"
    echo -e "  ./setup-infrastructure.sh ${CYAN}logs${NC}     - Show logs of all services"
    echo -e "  ./setup-infrastructure.sh ${WHITE}help${NC}     - Show this help message"
    echo ""

    show_service_urls
}

# Main script logic
case "${1:-help}" in
    start)
        start_services
        ;;
    stop)
        stop_services
        ;;
    restart)
        restart_services
        ;;
    cleanup)
        cleanup_services
        ;;
    status)
        show_status
        ;;
    logs)
        show_logs
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        print_error "Unknown command: $1"
        echo ""
        show_help
        exit 1
        ;;
esac
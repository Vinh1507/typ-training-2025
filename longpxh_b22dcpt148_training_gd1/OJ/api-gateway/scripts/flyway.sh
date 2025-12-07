#!/bin/bash

# Flyway Manual Migration Script
# This script loads environment variables from .env file and runs Flyway commands manually

set -e

# Get the directory where this script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Load environment variables from .env file if it exists
ENV_FILE="$PROJECT_ROOT/.env"
if [ -f "$ENV_FILE" ]; then
    echo "Loading environment variables from $ENV_FILE"
    set -a
    source "$ENV_FILE"
    set +a
else
    echo "Warning: .env file not found at $ENV_FILE"
fi

# Set default values if not set
export OJ_DB_URL="${OJ_DB_URL:-jdbc:postgresql://localhost:5432/oj_cpp}"
export OJ_DB_USERNAME="${OJ_DB_USERNAME:-oj_dev}"
export OJ_DB_PASSWORD="${OJ_DB_PASSWORD:-DevOJ@123}"
export FLYWAY_CLEAN_DISABLED="${FLYWAY_CLEAN_DISABLED:-false}"
export FLYWAY_BASELINE_ON_MIGRATE="${FLYWAY_BASELINE_ON_MIGRATE:-true}"
export FLYWAY_TABLE="${FLYWAY_TABLE:-migrations}"
export MIGRATION_MODULE="${MIGRATION_MODULE:-migration}"

# Change to the api-gateway directory
cd "$PROJECT_ROOT"

# Function to display usage
usage() {
    echo "Usage: $0 <command> [options]"
    echo ""
    echo "Commands:"
    echo "  migrate    - Apply all pending migrations"
    echo "  clean      - Drop all objects in the configured schemas"
    echo "  info       - Print current migration status"
    echo "  validate   - Validate applied migrations against available ones"
    echo "  baseline   - Baseline an existing database"
    echo "  repair     - Repair the schema history table"
    echo "  undo       - Undo the last migration (if supported)"
    echo ""
    echo "Options:"
    echo "  --module=<name>  - Specify migration module (default: migration)"
    echo ""
    echo "Examples:"
    echo "  $0 migrate"
    echo "  $0 info"
    echo ""
    echo "Environment variables loaded from .env:"
    echo "  OJ_DB_URL=$OJ_DB_URL"
    echo "  OJ_DB_USERNAME=$OJ_DB_USERNAME"
    echo "  OJ_DB_PASSWORD=***"
    echo "  MIGRATION_MODULE=$MIGRATION_MODULE"
}

# Check if command is provided
if [ $# -eq 0 ]; then
    usage
    exit 1
fi

COMMAND=$1

# Parse optional --module parameter
for arg in "$@"; do
    case $arg in
        --module=*)
            MIGRATION_MODULE="${arg#*=}"
            shift
            ;;
    esac
done

case $COMMAND in
    migrate|clean|info|validate|baseline|repair|undo)
        echo "Running Flyway $COMMAND..."
        mvn -pl $MIGRATION_MODULE \
            -Denv.OJ_DB_URL="$OJ_DB_URL" \
            -Denv.OJ_DB_USERNAME="$OJ_DB_USERNAME" \
            -Denv.OJ_DB_PASSWORD="$OJ_DB_PASSWORD" \
            flyway:$COMMAND -q
        echo "Flyway $COMMAND completed successfully."
        ;;
    *)
        echo "Error: Unknown command '$COMMAND'"
        usage
        exit 1
        ;;
esac

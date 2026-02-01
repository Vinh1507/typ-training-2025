
# Services to build
services="discovery-service config-service api-gateway auth-service core-service submission-service judge-service"

echo "🚀 Starting build for all services..."

for service in $services; do
    if [ -d "$service" ]; then
        echo "--------------------------------------------------"
        echo "Building $service..."
        echo "--------------------------------------------------"
        cd "$service"
        mvn clean package -DskipTests
        if [ $? -ne 0 ]; then
            echo "❌ Build failed for $service"
            exit 1
        fi
        cd ..
        echo "✅ $service built successfully"
    else
        echo "⚠️ Directory $service not found, skipping..."
    fi
done

echo "--------------------------------------------------"
echo "🎉 Maven build completed successfully!"
echo "--------------------------------------------------"

echo "🐳 Stopping existing containers..."
docker compose down

echo "🚀 Starting services with forced rebuild..."
docker compose up -d --build --force-recreate

echo "✅ System restarted! Please wait 1-2 minutes for services to fully initialize."

cd config-server
./gradlew jibDockerBuild
cd ..
cd licensing-service
./gradlew jibDockerBuild
cd ..
docker compose -f ./docker/docker-compose.yml up -d
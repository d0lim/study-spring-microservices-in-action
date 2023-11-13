cd config-server
./gradlew jibDockerBuild
cd ..
cd gateway-server
./gradlew jibDockerBuild
cd ..
cd eureka-server
./gradlew jibDockerBuild
cd ..
cd licensing-service
./gradlew jibDockerBuild
cd ..
cd organization-service
./gradlew jibDockerBuild
cd ..
docker compose -f ./docker/docker-compose.yml up -d --force-recreate
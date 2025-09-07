.\mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
.\mvnw spring-boot:run "-Dspring-boot.run.profiles=prod"

admin@example.com
admin

Run tests:
./mvnw -q test -Dtest=WorkoutRepositoryTest
./mvnw -q test -Dtest=SecurityFlowIntegrationTest
./mvnw -q test -Dtest=SecurityPublicEndpointsTest

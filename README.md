# StudentRiskBackend

Spring Boot backend for the Academic Risk Prediction project.

## Local run

1. Configure environment variables or use defaults in `application.properties`
2. Run:
   `mvn spring-boot:run`

## Render deployment

Build command:
`mvn clean package -DskipTests`

Start command:
`java -jar target/backend-1.0.0.jar`

If Render is configured as a Docker service, this repo now includes a `Dockerfile` and can be deployed directly without custom build/start commands.

## Required environment variables

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `APP_CORS_ALLOWED_ORIGINS`

## Optional environment variables

- `OPENROUTER_API_KEY`
- `OPENROUTER_API_URL`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `JWT_EXPIRATION`

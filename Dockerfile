# Stage 1: Build Frontend
FROM node:18-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 2: Build Backend
FROM maven:3.9-eclipse-temurin-11 AS backend-build
WORKDIR /app
COPY backend/pom.xml ./
COPY backend/src ./src

# Copy frontend build to Spring Boot static resources
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static

RUN mvn clean package -DskipTests

# Stage 3: Run
FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar

# Render.com uses PORT environment variable
ENV PORT=8080
EXPOSE 8080

# Use shell form for variable expansion
CMD java -jar app.jar --server.port=$PORT

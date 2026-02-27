# ---- Stage 1: Build ----
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy gradle wrapper and build files first (layer caching)
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (cached unless build files change)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src/ src/

# Build the fat jar, skip tests for faster builds
RUN ./gradlew bootJar --no-daemon -x test

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install wget for health checks
RUN apk add --no-cache wget

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check using actuator
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=40s \
  CMD wget --spider -q http://localhost:8080/actuator/health || exit 1

# JVM tuning for containers
# -XX:+UseContainerSupport  — respect container memory limits
# -XX:MaxRAMPercentage=75   — use up to 75% of container memory for heap
# -XX:+ExitOnOutOfMemoryError — crash fast on OOM instead of limping
# Graceful shutdown via SIGTERM is handled by Spring Boot
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]




# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21

ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app

COPY --from=builder /build/target/shopping-cart-*.jar /app/

# Set environment variables with defaults or from ARG
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

EXPOSE 5000

# Run the application with environment variables for profile and database
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${POSTGRES_URL} shopping-cart-${JAR_VERSION}.jar

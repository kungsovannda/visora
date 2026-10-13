# Build the entire project
FROM bellsoft/liberica-openjdk-debian:25 AS gradle-builder

WORKDIR /build

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# Cache dependencies
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon 2>/dev/null || true

# Copy all source and build
COPY . .

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :visora-main:bootJar \
    --no-daemon \
    --parallel \
    -x test

# Perform the extraction in a separate builder container
FROM bellsoft/liberica-openjre-debian:25-cds AS builder

WORKDIR /builder

COPY --from=gradle-builder /build/visora-main/build/libs/visora-main*.jar application.jar

# Extract the jar file using an efficient layout
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# This is the runtime container
FROM bellsoft/liberica-openjre-debian:25-cds

WORKDIR /application

# Default profile is prod, can be overridden at build or run time
ARG SPRING_PROFILES_ACTIVE=prod
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# Copy the extracted jar contents from the builder container into the working directory in the runtime container
# Every copy step creates a new docker layer
# This allows docker to only pull the changes it really needs
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

# Start the application jar - this is not the uber jar used by the builder
# This jar only contains application code and references to the extracted jar files
# This layout is efficient to start up and AOT cache (and CDS) friendly
ENTRYPOINT ["java", "-jar", "application.jar"]
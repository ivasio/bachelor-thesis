# Build context is project root
# Test and package
FROM maven:3.6.1-jdk-8-alpine as target
WORKDIR /build

# Install the dependencies
# All the poms must be listed here
COPY ./pom.xml ./pom.xml
COPY ./web_server/pom.xml ./web_server/pom.xml
COPY ./shared/pom.xml ./shared/pom.xml
COPY ./event_processing/pom.xml ./event_processing/pom.xml
COPY ./event_processing/aggregator/pom.xml ./event_processing/aggregator/pom.xml
COPY ./event_processing/publisher/pom.xml ./event_processing/publisher/pom.xml
RUN mvn dependency:go-offline

COPY . .
RUN mvn package

# Package image
FROM openjdk:8-jre
COPY --from=target /build/event_processing/publisher/target/publisher-*-with-dependencies.jar /
COPY --from=target /build/event_processing/publisher/setup.sh /
ENTRYPOINT /setup.sh && java -jar /publisher-*-with-dependencies.jar

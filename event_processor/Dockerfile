# Build context is project root
# Test and package
FROM maven:3.6.1-jdk-8-alpine as target
WORKDIR /build

# Install the dependencies
# All the poms must be listed here
COPY ./pom.xml ./pom.xml
COPY ./web_server/pom.xml ./web_server/pom.xml
COPY ./shared/pom.xml ./shared/pom.xml
COPY ./event_processor/pom.xml ./event_processor/pom.xml
RUN mvn dependency:go-offline verify --fail-never

COPY . .
RUN mvn -pl .,shared,event_processor package

# Package image
FROM openjdk:8-jre-alpine
COPY --from=target /build/event_processor/target/event_processor-*.jar /event_processor-*.jar
ENTRYPOINT java -jar /event_processor-*.jar

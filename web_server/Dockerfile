# Build context is project root
# Test and package
FROM maven:3.6.1-jdk-8-alpine as target
WORKDIR /build

# Install the dependencies
# All the poms must be listed here
COPY ./pom.xml ./
COPY ./web_server/pom.xml ./web_server/
COPY ./shared/pom.xml ./shared/
RUN mvn dependency:go-offline

COPY . .
RUN mvn package

# Package image
FROM openjdk:8-jre-alpine
COPY --from=target /build/web_server/target/web_server-0.1.jar /web_server-0.1.jar
ENTRYPOINT java -jar /web_server-0.1.jar

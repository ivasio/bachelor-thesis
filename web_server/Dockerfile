# Build context is project root
FROM maven:3.6.1-jdk-8-alpine

WORKDIR /app
COPY . .
RUN mvn clean package

ENTRYPOINT java -jar web_server/target/web_server-0.1.jar

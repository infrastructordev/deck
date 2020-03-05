FROM openjdk:11

EXPOSE 8080

COPY ./target/deck-api-*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

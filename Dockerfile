FROM openjdk

ARG JAR_FILE=target/KameleoonTrialTask-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
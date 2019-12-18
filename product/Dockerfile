FROM maven:3.5.3-jdk-8-alpine as TARGET
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ src/
RUN mvn package -DskipTests

FROM openjdk:8-jre-alpine
COPY --from=TARGET /target/*.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
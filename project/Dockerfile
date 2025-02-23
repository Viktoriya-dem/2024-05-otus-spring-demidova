FROM openjdk:17

COPY ./ /opt
WORKDIR /opt

ENTRYPOINT ["java", "-jar", "target/recipe-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080/tcp
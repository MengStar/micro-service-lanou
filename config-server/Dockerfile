FROM java:8
ADD target/config-server.jar /app.jar
EXPOSE 1100
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
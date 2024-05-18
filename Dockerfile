FROM openjdk:21
WORKDIR /app
COPY target/ButcherShop-1.0-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]
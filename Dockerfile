FROM eclipse-temurin:17-jdk-focal
LABEL authors="Omar"

COPY target/products-accounts-0.1.jar products-accounts-0.1.jar
ENTRYPOINT ["java","-jar","/products-accounts-0.1.jar"]

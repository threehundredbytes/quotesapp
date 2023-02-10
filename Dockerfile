FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY /build/libs/quotesapp-1.0.0.jar ./quotesapp-1.0.0.jar

ENTRYPOINT ["java", "-jar", "quotesapp-1.0.0.jar"]
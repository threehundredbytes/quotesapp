# quotesapp

A Spring Boot REST API

# How to run

You can run application using Java:

    gradlew clean build
    java -jar /build/libs/quotesapp-1.0.0.jar

Or build Docker image and run it:

    gradlew clean build
    docker build --tag=quotesapp:1.0.0 .
    docker run -p 8080:8080 quotesapp:1.0.0

Or pull Docker image from DockerHub and run it:

    docker image pull threehundredbytes/quotesapp:latest
    docker run -p 8080:8080 threehundredbytes/quotesapp:latest


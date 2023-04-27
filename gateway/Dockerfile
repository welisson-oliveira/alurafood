FROM openjdk:17-alpine
RUN apk add --update curl && \
    rm -rf /var/cache/apk/*
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# minikube docker-env
# eval $(minikube -p minikube docker-env)
# docker build -t gateway -f ./Dockerfile .

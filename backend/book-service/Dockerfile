FROM openjdk:21-ea-17-jdk-slim
COPY build/libs/book-service-0.0.1-SNAPSHOT.jar book-service.jar

RUN apt update && apt install curl -y && apt install jq -y
COPY entrypoint.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
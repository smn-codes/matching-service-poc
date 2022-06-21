FROM amazoncorretto:8-alpine3.14-jre

USER root

RUN mkdir -p /hicounselor

WORKDIR /hicounselor

COPY ./router-service/build/libs/router-service-*.jar /hicounselor/app.jar

ENTRYPOINT ["java", "-jar", "/hicounselor/app.jar"]
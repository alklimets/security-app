FROM openjdk:17

WORKDIR /tmp/app
COPY target/application-*.jar /tmp/app/app.jar

EXPOSE 8081
ENV JAVA_OPTS=""

CMD java ${JAVA_OPTS} -jar /tmp/app/app.jar
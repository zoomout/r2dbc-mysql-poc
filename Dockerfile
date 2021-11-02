FROM openjdk:11

VOLUME /app
RUN mkdir /app
COPY target/myservice-*.jar app.jar
RUN echo "java \$@ -jar -Dreactor.netty.http.server.accessLogEnabled=true /app.jar" > /app/entrypoint.sh
ENTRYPOINT ["/bin/sh", "/app/entrypoint.sh"]

FROM eclipse-temurin:25-jre AS java

USER root

WORKDIR /app
COPY --chmod=644 --chown=root:root ./target/spring-admin-*.jar app.jar

USER 1001

ARG OTEL_SERVICE_NAME
ARG OTEL_SERVICE_NAMESPACE
ARG JAVA_VM_OPTIONS
ARG PORT
ARG MANAGEMENT_PORT
ARG SPRING_PROFILES_ACTIVE

ENV OTEL_SERVICE_NAME="spring-admin"
ENV OTEL_SERVICE_NAMESPACE="app"
ENV JAVA_VM_OPTIONS="-XX:MaxRAMPercentage=80.0 -Dfile.encoding=UTF-8"
ENV PORT="8080"
ENV MANAGEMENT_PORT="8081"
ENV SPRING_PROFILES_ACTIVE="container"


ENTRYPOINT ["sh", "-c"]
CMD ["exec java $JAVA_VM_OPTIONS -jar /app/app.jar"]

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 CMD sh -c "wget --no-verbose --tries=1 --spider http://localhost:$MANAGEMENT_PORT/actuator/health || exit 1"

EXPOSE 8080 8081
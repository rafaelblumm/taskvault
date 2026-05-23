# Imagem Alpine com instalação do Java 25
FROM eclipse-temurin:25.0.3_9-jre-alpine-3.23

# Cria usuário de aplicação e altera usuário corrente
RUN addgroup -S appuser && \
    adduser -S appuser -G appuser
USER appuser:appuser

COPY target/taskvault-server-0.0.1.jar taskvault-server.jar

ENTRYPOINT [ "java", "-jar", "taskvault-server.jar" ]

# Imagen liviana solo con Java 17 para ejecutar la API bancaria
FROM eclipse-temurin:17-jre-jammy

# Usuario sin privilegios (no ejecutar como root)
RUN groupadd --system spring && useradd --system --gid spring spring

WORKDIR /app

# Copiar SOLO el JAR construido por el pipeline (artefacto)
COPY target/banco2025.jar banco2025.jar

USER spring
EXPOSE 8080
# Limita la memoria de la JVM al contenedor (plan gratuito de Render: 512 MB)
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","banco2025.jar"]

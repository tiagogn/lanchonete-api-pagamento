FROM gradle:8-jdk17-alpine AS build

WORKDIR /home/gradle/src

COPY build.gradle settings.gradle ./

RUN gradle clean build --no-daemon > /dev/null 2>&1 || true

COPY . .

RUN gradle clean build --no-daemon -x test

FROM openjdk:17-alpine

# Copiar o arquivo global-bundle.pem para dentro do contêiner
COPY manifests/api/global-bundle.pem /tmp/global-bundle.pem

# Importar o certificado no keystore padrão da JVM
RUN keytool -import -alias amazon -keystore $JAVA_HOME/lib/security/cacerts -file /tmp/global-bundle.pem -noprompt -storepass changeit

EXPOSE 8082

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/lanchonete-pagamento.jar

ENTRYPOINT ["java","-jar","/app/lanchonete-pagamento.jar"]
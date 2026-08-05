FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
COPY gradle.properties /root/.gradle/gradle.properties
COPY src ./src

ENV TZ=Asia/Phnom_Penh

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jdk-alpine

ENV TZ=Asia/Phnom_Penh

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8090

ENTRYPOINT ["java","-Duser.timezone=Asia/Phnom_Penh","-jar","/app/app.jar"]
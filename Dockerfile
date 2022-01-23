FROM openjdk:11.0.11-jre

COPY app.jar app.jar

# 暴露端口
EXPOSE 8080

ENTRYPOINT [ "java" , "-jar" , "-Djava.awt.headless=true" , "-XX:+UseContainerSupport" , "/app.jar" ]
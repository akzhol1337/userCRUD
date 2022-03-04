FROM openjdk:11
ADD build/libs/*.jar dockerapp.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "dockerapp.jar"]
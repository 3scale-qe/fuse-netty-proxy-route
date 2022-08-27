FROM registry.access.redhat.com/ubi8/openjdk-8
EXPOSE 8088
EXPOSE 8443
ARG JAR_FILE=target/*.jar
COPY keystore.jks keystore.jks
COPY ${JAR_FILE} fuse-proxy.jar
ENTRYPOINT ["java","-jar","fuse-proxy.jar"]
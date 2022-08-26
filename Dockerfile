FROM registry.access.redhat.com/ubi8/openjdk-8 as builder
WORKDIR /home/jboss/src
COPY . .
RUN mvn --batch-mode --update-snapshots package

FROM registry.access.redhat.com/ubi8/openjdk-8
EXPOSE 8088
EXPOSE 8443
ARG JAR_FILE=target/*.jar
COPY keystore.jks keystore.jks
COPY --from=builder /home/jboss/src/target/fuse-camel-proxy-com.redhat.camelProxy.jar fuse-proxy.jar
ENTRYPOINT ["java","-jar","fuse-proxy.jar"]

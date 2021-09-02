# Fuse Camel proxy route
The proxy route uses [Apache Camel](https://camel.apache.org/components/2.x/) integration framework to create HTTP proxy usign [Camel HTTP4](https://camel.apache.org/components/2.x/http4-component.html) component. The project is based on [Camel Netty Proxy example](https://github.com/zregvart/camel-netty-proxy) project developed by Zoran Regvart. The main difference is that this project is based on Fuse repositories (dependencies are defiend by [Fuse BOM file](https://mvnrepository.com/artifact/org.jboss.redhat-fuse/fuse-springboot-bom?repo=redhat-ga)), with Camel version 2.23. In contrast with Zorans example, that uses Camel 3.

The proxy offers two Camel routes on different ports:
1. 8088: Simple HTTP route
2. 8443: The support for HTTP over TLS (https) protocol is available if Java Keystore file is mounted at /keystore.jks (with password changeit). The implementation doesn't support HTTPS proxy tunneling via CONNECT, the request needs to be issued same as it is issued for the HTTP PROXY, the only added benefit is that the request can be made over TLS.

Both routes add some headers to verify if the proxy was used during communication.

## Running on Openshift
The main requirement for this proxy was the ability to be deployed to the Openshift as application from an existing image.

1. Log in your OpenShift cluster with command `oc login` and create a new Openshift project with `oc new-project <project_name>`.

2. Build the Docker image execute:

        $ docker build -t fuse-camel-proxy .

3. Push the image to your prefered container image registry:

        $ docker push <hub-user>/fuse-camel-proxy

4. Create Openshift new application

        $ oc new-app <hub-user>/fuse-camel-proxy

## Running the Fuse Camel proxy locally:
Build the project:

    $ mvn clean package

Run the Camel route:

    $ mvn spring-boot:run

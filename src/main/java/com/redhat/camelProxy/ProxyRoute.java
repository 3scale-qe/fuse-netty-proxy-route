/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.redhat.camelProxy;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@SpringBootApplication
public class ProxyRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        final RouteDefinition route =
                from("netty4-http:proxy://0.0.0.0:8088");
        createRoute(route);

        final RouteDefinition routeTLS =
                from("netty4-http:proxy://0.0.0.0:8443?ssl=true&keyStoreFile=keystore.jks&passphrase=changeit&trustStoreFile=keystore.jks");
        createRoute(routeTLS);
    }

    private void createRoute(RouteDefinition route) {
        route.process(ProxyRoute::saveHostHeader)
                .process(ProxyRoute::addCustomHeader)
                .toD("netty4-http:"
                        + "${headers." + Exchange.HTTP_SCHEME + "}://"
                        + "${headers." + Exchange.HTTP_HOST + "}:"
                        + "${headers." + Exchange.HTTP_PORT + "}"
                        + "${headers." + Exchange.HTTP_PATH + "}")
                .process(ProxyRoute::addCustomHeader);
    }

    private static void addCustomHeader(final Exchange exchange) {
        final Message message = exchange.getIn();
        final String body = message.getBody(String.class);
        System.out.println("HEADERS: " + message.getHeaders());
        message.setHeader("Fuse-Camel-Proxy", "Request was redirected to Camel netty4 proxy service");
        message.setBody(body);
        System.out.println(body);
    }

    private static void saveHostHeader(final Exchange exchange) {
        final Message message = exchange.getIn();
        System.out.println("HEADERS: " + message.getHeaders());
        String hostHeader = message.getHeader("Host", String.class);
        message.setHeader("Source-Header", hostHeader);
    }

}

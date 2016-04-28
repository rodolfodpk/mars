package com.rdpk.mars.web;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class MarsMissionRestRoute extends RouteBuilder {

    public static final String JSON_CONTENT = "application/json";

    @Override
    public void configure() throws Exception {

        restConfiguration().component("servlet")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "Mothership API").apiProperty("api.version", "0.1")
                .dataFormatProperty("prettyPrint", "true")
                // and enable CORS
                .apiProperty("cors", "true")
                .host("localhost").port(8080)
                .bindingMode(RestBindingMode.json);

        rest("/commands")
                .id("commands-rest-route")
                .consumes(JSON_CONTENT).produces(JSON_CONTENT)
                .post("/mars").type(MarsRequest.class).description("Submit a command").outType(MarsResponse.class).description("Plateau status")
                .param().name("body").description("The command").endParam()
                .to("direct:process-cmd-request");

        from("direct:process-cmd-request")
            .routeId("commands-processing-route")
            .log("received --> ${body}");

    }

}
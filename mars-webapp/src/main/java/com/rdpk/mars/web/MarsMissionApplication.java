package com.rdpk.mars.web;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.rdpk.mars.Mission;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.val;

import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class MarsMissionApplication extends Application<MarsMissionConfiguration> {

    public static void main(String... args) throws Exception {
        new MarsMissionApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<MarsMissionConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<MarsMissionConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(MarsMissionConfiguration marsMissionConfiguration) {
                // this would be the preferred way to set up swagger, you can also construct the object here programtically if you want
                return marsMissionConfiguration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(MarsMissionConfiguration configuration, Environment environment) throws Exception {
        // add your resources as usual
        val mapper = environment.getObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        mapper.registerModule(new Jdk8Module());
        environment.jersey().register(new MarsMissionResource(new MarsMissionController(new Mission(), new AtomicInteger())));
    }
}

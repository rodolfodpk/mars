package com.rdpk.mars.web;

import com.rdpk.mars.MarsMissionController;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

/**
 */
public class MarsMissionApplication extends Application<MarsConfiguration> {

    public static void main(String... args) throws Exception {
        new MarsMissionApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<MarsConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<MarsConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(MarsConfiguration marsConfiguration) {
                // this would be the preferred way to set up swagger, you can also construct the object here programtically if you want
                return marsConfiguration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(MarsConfiguration configuration, Environment environment) throws Exception {
        // add your resources as usual
        environment.jersey().register(new MarsResource(new MarsMissionController()));
    }
}

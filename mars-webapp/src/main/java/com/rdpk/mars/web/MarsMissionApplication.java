package com.rdpk.mars.web;

import com.rdpk.mars.MarsMissionController;
import com.rdpk.mars.Mission;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class MarsMissionApplication extends Application<MarsMIssionConfiguration> {

    public static void main(String... args) throws Exception {
        new MarsMissionApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<MarsMIssionConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<MarsMIssionConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(MarsMIssionConfiguration marsMIssionConfiguration) {
                // this would be the preferred way to set up swagger, you can also construct the object here programtically if you want
                return marsMIssionConfiguration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(MarsMIssionConfiguration configuration, Environment environment) throws Exception {
        // add your resources as usual
        environment.jersey().register(new MarsMissionResource(new MarsMissionController(new Mission(), new AtomicInteger())));
    }
}

package com.rdpk.mars.web;

import com.rdpk.mars.MarsMissionController;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MarsMissionCtxListener implements ServletContextListener {

    CamelContext camel ;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.camel = new DefaultCamelContext();
        try {
            camel.addRoutes(new MarsMissionRestRoute(new MarsMissionController()));
            camel.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            camel.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

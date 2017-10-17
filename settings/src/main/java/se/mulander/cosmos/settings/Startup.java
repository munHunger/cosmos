package se.mulander.cosmos.settings;

import se.mulander.cosmos.common.ssl.SSLManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Marcus Münger on 2017-05-19.
 */
public class Startup implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            SSLManager.init();
        } catch (Exception e) {
            System.err.println("Could not initialize SSL");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }
}
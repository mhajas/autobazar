package PB138.project.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import PB138.project.database.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sun.tools.jar.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.*;


@WebListener
public class StartListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent ev) {

        configureLogging();
        log.log(Level.INFO, "Aplikacia inicializovana");
        ServletContext servletContext = ev.getServletContext();
        ApplicationContext springContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        servletContext.setAttribute("carManager", springContext.getBean("carManager", CarManager.class));
        log.log(Level.INFO, "Manazery vytvorene");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        log.log(Level.INFO, "Aplikacia konci");
    }
    private static void configureLogging() {
        Handler fileHandler = null;
        Properties prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = CarManager.class.getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException ex){
                log.log(Level.SEVERE, "Cannot load property from input stream.");
                throw new IllegalArgumentException("Cannot load property from input stream.");
            }
        } else {
            log.log(Level.SEVERE, "property file '" + propFileName + "' not found in the classpath");
            throw new IllegalArgumentException("property file '" + propFileName + "' not found in the classpath");
        }
        try {
            fileHandler = new FileHandler(prop.getProperty("logFile"));
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Unable to initialize FileHandler", ex);
        } catch (SecurityException ex) {
            log.log(Level.SEVERE, "Unable to initialize FileHandler.", ex);
        }

        Logger.getLogger("").addHandler(fileHandler);
    }
}
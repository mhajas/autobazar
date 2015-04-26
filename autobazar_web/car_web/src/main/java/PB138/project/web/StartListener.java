package PB138.project.web;

/**
 * Created by Matej on 26. 4. 2015.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import PB138.project.database.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@WebListener
public class StartListener implements ServletContextListener {

    final static Logger log = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        log.info("aplikace inicializována");
        ServletContext servletContext = ev.getServletContext();
        ApplicationContext springContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        servletContext.setAttribute("carManager", springContext.getBean("carManager", CarManager.class));
        log.info("vytvořeny manažery");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        log.info("aplikace končí");
    }
}
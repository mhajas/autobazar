package PB138.project.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import sun.tools.jar.Main;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Spring Java configuration class. See http://static.springsource.org/spring/docs/current/spring-framework-reference/html/beans.html#beans-java
 *
 * @author Martin Kuba makub@ics.muni.cz
 */

@Configuration  //je to konfigurace pro Spring
public class SpringConfig {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    @Bean
    public Collection dataSource(){
        Collection collection;
        try {
            collection = DBUtils.loadOrCreateCarCollection();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | XMLDBException e) {
            log.log(Level.SEVERE, "Exception in SpringConfig:"+e);
            throw new DBException("Error while getting collection from DB",e);
        }

        return collection;
    }

    @Bean
    public CarManager carManager() {
        return new CarManagerImpl(dataSource());
    }
}

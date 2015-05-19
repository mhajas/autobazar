package PB138.project.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;


/**
 * Spring Java configuration class. See http://static.springsource.org/spring/docs/current/spring-framework-reference/html/beans.html#beans-java
 *
 * @author Martin Kuba makub@ics.muni.cz
 */

@Configuration  //je to konfigurace pro Spring
public class SpringConfig {

    @Bean
    public Collection dataSource(){
        Collection collection;
        try {
            collection = DBUtils.loadOrCreateCarCollection();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | XMLDBException e) {
            throw new DBException("Error while getting collection from DB",e);
        }

        return collection;
    }

    @Bean //náš manager, bude obalen řízením transakcí
    public CarManager carManager() {
        return new CarManagerImpl(dataSource());
    }
}

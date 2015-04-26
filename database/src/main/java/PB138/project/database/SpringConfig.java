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

    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    private static final String PREFIX = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";

    @Bean
    public Collection dataSource(){
        Collection collection;
        try {
            Class c = Class.forName(DRIVER);
            Database database = (Database) c.newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);
            Collection parent = DatabaseManager.getCollection(PREFIX,"admin","test123");
            CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
            mgt.createCollection("cars");
            parent.close();
            collection = DatabaseManager.getCollection(PREFIX + "cars", "admin", "test123");

            XMLResource resource = (XMLResource)collection.createResource("cars.xml", "XMLResource");
            resource.setContent("<cars><car id=\"1\">" +
                    "        <manufacturer>Ferrari</manufacturer>" +
                    "        <km>80000</km>" +
                    "        <price>150000</price>" +
                    "        <color>blue</color>" +
                    "        <description>OK stav</description>" +
                    "    </car></cars>");
            collection.storeResource(resource);

            resource = (XMLResource)collection.createResource("data.xml", "XMLResource");
            resource.setContent("<data><car-next-id>1</car-next-id></data>");
            collection.storeResource(resource);


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

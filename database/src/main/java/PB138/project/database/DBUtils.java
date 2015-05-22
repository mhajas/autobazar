package PB138.project.database;

import org.exist.xmldb.XQueryService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Created by Michal on 15.5.2015.
 */
public class DBUtils {

    public static Collection<Car> selectCarsFromDBWhere(org.xmldb.api.base.Collection collection, String condition, String[] arguments) throws XMLDBException{
        List<Car> resultList = new ArrayList<>();
        String xQuery;

        if(condition != null && !condition.isEmpty()) {
            xQuery = "let $doc := doc($document) " +
                   "return $doc/cars/car[" + condition + "]";
        }else{
            xQuery = "let $doc := doc($document) " +
                    "return $doc/cars/car";
        }

        XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");

        service.declareVariable("document", "/db/cars/cars.xml");

        if(arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                service.declareVariable("argument" + i, arguments[i]);
            }
        }

        service.setProperty("indent", "yes");
        CompiledExpression compiled = service.compile(xQuery);

        ResourceSet res = service.execute(compiled);
        ResourceIterator it = res.getIterator();
        while(it.hasMoreResources()){
            Resource resource = it.nextResource();
            resultList.add(parseCarFromXML(resource.getContent().toString()));
        }
        return resultList;
    }

    public static Collection<Car> selectCarsFromDBWhere(org.xmldb.api.base.Collection collection) throws XMLDBException{
        return selectCarsFromDBWhere(collection, null, null);
    }

    public static Collection<Car> selectCarsFromDBWhere(org.xmldb.api.base.Collection collection, String condition) throws XMLDBException{
        return selectCarsFromDBWhere(collection, condition, null);
    }

    public static void bindCarToXQuery(Car car, XQueryService service){
        try {
            service.declareVariable("id", car.getId());
            service.declareVariable("manufacturer", car.getManufacturer());
            service.declareVariable("km", car.getKm());
            service.declareVariable("price", car.getPrice());
            service.declareVariable("color", car.getColor());
            if (car.getDescription() == null) {
                service.declareVariable("description", "");
            } else {
                service.declareVariable("description", car.getDescription());
            }
        }catch(XMLDBException ex){
            throw new DBException("Error while binding car.", ex);

        }
    }

    public static Long getNextId(org.xmldb.api.base.Collection collection){
        try {
            String xQuery = "let $doc := doc($document)" +
                    "return $doc//car-next-id/text()";

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.declareVariable("document", "/db/cars/data.xml");
            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            ResourceSet resultSet = service.execute(compiled);
            ResourceIterator results = resultSet.getIterator();
            if(results.hasMoreResources()) {
                Long id = Long.parseLong(results.nextResource().getContent().toString());
                if(results.hasMoreResources()){
                    throw new DBException("data.xml has more car-next-id element");
                }
                return id;
            }else{
                throw new DBException("Next id does not exist");
            }
        }catch (XMLDBException ex) {
            throw new DBException("Error while getting next id", ex);
        }catch (NumberFormatException ex){
            throw new DBException("Error while parsing next id", ex);
        }
    }

    public static void incrementId(org.xmldb.api.base.Collection collection, Long id) {
        try {
            String xQuery = "let $doc := doc($document)" +
                    "return update value $doc//car-next-id with $nextId";

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.declareVariable("document", "/db/cars/data.xml");
            service.declareVariable("nextId",++id);
            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            service.execute(compiled);
        }catch (XMLDBException ex) {
            throw new DBException("Error while incrementing", ex);
        }catch (NumberFormatException ex){
            throw new DBException("Error while parsing next id", ex);
        }
    }

    public static Car parseCarFromXML(String xml){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            try {
                Car car = new Car();
                Document doc = db.parse(is);
                NodeList a = doc.getElementsByTagName("car");
                Element parent = (Element) a.item(0);
                car.setId(Long.parseLong(parent.getAttribute("id")));

                a = parent.getElementsByTagName("manufacturer");
                if(a.getLength() != 1){
                    throw new CarException("Error while parsing manufacturer");
                }
                Element el = (Element) a.item(0);
                car.setManufacturer(el.getTextContent());

                a = parent.getElementsByTagName("km");
                if(a.getLength() != 1){
                    throw new CarException("Error while parsing km");
                }
                el = (Element) a.item(0);
                try {
                    car.setKm(Integer.parseInt(el.getTextContent()));
                }catch(NumberFormatException ex){
                    throw new CarException("Error while parsing integer");
                }

                a = parent.getElementsByTagName("price");
                if(a.getLength() != 1){
                    throw new CarException("Error while parsing price");
                }
                el = (Element) a.item(0);
                car.setPrice(new BigDecimal(el.getTextContent()));

                a = parent.getElementsByTagName("color");
                if(a.getLength() != 1){
                    throw new CarException("Error while parsing color");
                }
                el = (Element) a.item(0);
                car.setColor(el.getTextContent());

                a = parent.getElementsByTagName("description");
                if(a.getLength() != 1){
                    throw new CarException("Error while parsing description");
                }
                el = (Element) a.item(0);
                if(!el.getTextContent().isEmpty()){
                    car.setDescription(el.getTextContent());
                }
                return car;

            } catch (SAXException e) {
                throw new CarException("Error creating document from xml for parsing");
            } catch (IOException e) {
                throw new CarException("Error parsing car");
            }
        } catch (ParserConfigurationException ex) {
            throw new CarException("Error while configure parser", ex);
        }
    }

    public static org.xmldb.api.base.Collection loadOrCreateCarCollection() throws IllegalAccessException, InstantiationException, ClassNotFoundException, XMLDBException {
        Properties configProperty = new ConfigProperty();

        Class c = Class.forName(configProperty.getProperty("db_driver"));
        Database database = (Database) c.newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);

        org.xmldb.api.base.Collection collection = DatabaseManager.getCollection(configProperty.getProperty("db_prefix") + configProperty.getProperty("db_collection"), configProperty.getProperty("db_name"), configProperty.getProperty("db_password"));

        if (collection == null) {
            org.xmldb.api.base.Collection parent = DatabaseManager.getCollection(configProperty.getProperty("db_prefix"), configProperty.getProperty("db_name"), configProperty.getProperty("db_password"));
            CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
            mgt.createCollection(configProperty.getProperty("db_collection"));
            parent.close();
            collection = DatabaseManager.getCollection(configProperty.getProperty("db_prefix") + configProperty.getProperty("db_collection"), configProperty.getProperty("db_name"), configProperty.getProperty("db_password"));

            XMLResource resource = (XMLResource) collection.createResource(configProperty.getProperty("db_carResourceName"), "XMLResource");
            resource.setContent("<cars></cars>");
            collection.storeResource(resource);

            resource = (XMLResource) collection.createResource(configProperty.getProperty("db_metaData"), "XMLResource");
            resource.setContent("<data><car-next-id>1</car-next-id></data>");
            collection.storeResource(resource);
        }

        return collection;
    }

    public static void dropCarDatabase() throws XMLDBException{
        Properties configProperty = new ConfigProperty();

        org.xmldb.api.base.Collection parent = DatabaseManager.getCollection(configProperty.getProperty("db_prefix"),configProperty.getProperty("db_name"),configProperty.getProperty("db_password"));
        CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");

        mgt.removeCollection(configProperty.getProperty("db_prefix") + configProperty.getProperty("db_collection"));
        parent.close();
    }

}

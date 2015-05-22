package PB138.project.database;
import org.exist.xmldb.XQueryService;
import org.xmldb.api.base.*;
import sun.tools.jar.Main;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Michal on 24.4.2015.
 */
public class CarManagerImpl implements CarManager {

    private org.xmldb.api.base.Collection collection;

    public CarManagerImpl(org.xmldb.api.base.Collection collection) {
        this.collection = collection;
    }

    private static final Logger log = Logger.getLogger(Main.class.getName());

    @Override
    public void createCar(Car car) {
        checkCar(car);
        log.log(Level.INFO, "Create car in car manager: "+car);
        if(car.getId() != null){
            log.log(Level.SEVERE, "Car exception: Car id must be null");
            throw new CarException("Car id must be null");
        }
        car.setId(DBUtils.getNextId(collection));
        try {
            String xQuery = "let $doc := doc($document)" +
                    "return update insert element car{ " +
                    "attribute id {$id}, " +
                    "element manufacturer {$manufacturer}, " +
                    "element km {$km}, " +
                    "element price {$price}, " +
                    "element color {$color}, " +
                    "element description {$description}" +
                    "} into $doc/cars";
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");

            service.declareVariable("document", "/db/cars/cars.xml");
            DBUtils.bindCarToXQuery(car, service);

            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            service.execute(compiled);
            log.log(Level.INFO, "Create car in car manager "+car+ " is ok.");
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "DB exception: "+ex);
            throw new DBException("Error while creating new car",ex);
        }

        DBUtils.incrementId(collection, car.getId());
    }

    private void checkCar(Car car){
        if(car == null){
            log.log(Level.SEVERE, "Car exception in checkCar: Car is null");
            throw new CarException("Car is null");
        }

        if (car.getManufacturer() == null) {
            log.log(Level.SEVERE, "Car exception in checkCar: Car manufacturer is null");
            throw new CarException("Car manufacturer is null");
        }

        if (car.getManufacturer().isEmpty()) {
            log.log(Level.SEVERE, "Car exception in checkCar: Car manufacturer is empty");
            throw new CarException("Car manufacturer is empty");
        }

        if(car.getKm() < 0){
            log.log(Level.SEVERE, "Car exception in checkCar: Car km is less than zero");
            throw new CarException("Car km is less than zero");
        }

        if(car.getPrice() == null){
            log.log(Level.SEVERE, "Car exception in checkCar: Car price is null");
            throw new CarException("Car price is null");
        }

        if(car.getPrice().compareTo(new BigDecimal(0)) < 0){
            log.log(Level.SEVERE, "Car exception in checkCar: Car price is negative");
            throw new CarException("Car price is negative");
        }

        if(car.getColor() == null){
            log.log(Level.SEVERE, "Car exception in checkCar: Car color is null");
            throw new CarException("Car color is null");
        }

        if(car.getColor().isEmpty()){
            log.log(Level.SEVERE, "Car exception in checkCar: Car color is empty");
            throw new CarException("Car color is empty");
        }
    }

    @Override
    public Car getCarById(Long id) {
        log.log(Level.INFO, "Get car by ID "+id+" in car manager");
        if(id == null){
            log.log(Level.SEVERE, "Car exception : id is null");
            throw new CarException("id is null");
        }
        if(id < 0){
            log.log(Level.SEVERE, "Car exception : id is negative");
            throw new CarException("id is negative");
        }
        try {
            String xQuery = "let $doc := doc($document)" +
                    "return $doc/cars/car[@id=$id]";
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");

            service.declareVariable("document", "/db/cars/cars.xml");
            service.declareVariable("id", id);

            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            ResourceSet res = service.execute(compiled);
            ResourceIterator it = res.getIterator();
            if(it.hasMoreResources()){
                Resource resource = it.nextResource();
                Car result = DBUtils.parseCarFromXML(resource.getContent().toString());
                if(it.hasMoreResources()){
                    log.log(Level.SEVERE, "Car exception : More car with same id");
                    throw new CarException("More car with same id");
                }
                log.log(Level.INFO, "Get car by ID is OK");
                return result;
            }
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while creating new car",ex);
        }
        return null;
    }

    @Override
    public Collection<Car> getAllCars() {
        log.log(Level.INFO, "Get all cars in car manager");

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection);
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while getting all cars", ex);
        }
        log.log(Level.INFO, "Get all cars is OK");
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByManufacturer(String manufacturer) {
        log.log(Level.INFO, "Get car by manufacturer " +manufacturer+" in car manager");
        if(manufacturer == null){
            log.log(Level.SEVERE, "IlegalArgumentException : manufacturer is null");
            throw new IllegalArgumentException("manufacturer is null");
        }

        if(manufacturer.isEmpty()){
            log.log(Level.SEVERE, "IlegalArgumentException : is empty");
            throw new IllegalArgumentException("manufacturer is empty");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "manufacturer=$argument0", new String[]{manufacturer});
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while getting cars by manufacturer", ex);
        }
        log.log(Level.INFO, "Get car by manufacturer is OK");
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByColor(String color) {
        log.log(Level.INFO, "Get car by color "+color+" in car manager");

        if(color== null){
            log.log(Level.SEVERE, "IlegalArgumentException : color is null");
            throw new IllegalArgumentException("color is null");
        }

        if(color.isEmpty()){
            log.log(Level.SEVERE, "IlegalArgumentException : is empty");
            throw new IllegalArgumentException("color is empty");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "color=$argument0", new String[]{color});
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while getting cars by color", ex);
        }
        log.log(Level.INFO, "Get car by color is OK");
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByKmLessThan(int km) {
        log.log(Level.INFO, "Get car by km less than "+km+" in car manager");

        if(km < 0){
            log.log(Level.SEVERE, "IlegalArgumentException : km is negative");
            throw new IllegalArgumentException("km is negative");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "km<$argument0", new String[]{String.valueOf(km)});
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while getting cars less km", ex);
        }
        log.log(Level.INFO, "Get car by km less than is OK");
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByKmMoreThan(int km) {
        log.log(Level.INFO, "Get car by km more than "+km+" in car manager");
        if(km < 0){
            log.log(Level.SEVERE, "IlegalArgumentException : km is negative");
            throw new IllegalArgumentException("km is negative");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "km>$argument0", new String[]{String.valueOf(km)});
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while getting cars less km", ex);
        }
        log.log(Level.INFO, "Get car by km more than is OK");
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByKm(int from, int to) {
        log.log(Level.INFO, "Get car by km from "+ from+" to "+ to +" in car manager");

        if(from < 0){
            log.log(Level.SEVERE, "IlegalArgumentException : km from is negative");
            throw new IllegalArgumentException("from is negative");
        }
        if(to < 0){
            log.log(Level.SEVERE, "IlegalArgumentException : km to is negative");
            throw new IllegalArgumentException("to is negative");
        }
        if(to < from){
            log.log(Level.SEVERE, "IlegalArgumentException : to is less than from");
            throw new IllegalArgumentException("to is less than from");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "km>=number($argument0) and km<=number($argument1)", new String[]{String.valueOf(from), String.valueOf(to)});
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while getting cars less km", ex);
        }
        log.log(Level.INFO, "Get car by km is OK");
        return resultList;
    }

    @Override
    public Collection<Car> getCarsBySearchEngine(SearchEngine searchEngine) {
        log.log(Level.INFO, "Get car search engine in car manager");

        if(searchEngine == null){
            log.log(Level.SEVERE, "IlegalArgumentException : search engine is null");
            throw new IllegalArgumentException("search engine is null");
        }

        if(searchEngine.getSerializedConditions().isEmpty()){
            log.log(Level.SEVERE, "IlegalArgumentException : search engine is empty");
            throw new IllegalArgumentException("search engine is empty");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, searchEngine.getSerializedConditions(), searchEngine.getArgumentsArray());
        }catch(XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while getting cars by " + searchEngine, ex);
        }
        log.log(Level.INFO, "Get car search engine is OK");
        return resultList;
    }

    @Override
    public void updateCar(Car car) {
        log.log(Level.INFO, "Update car "+car+" in car manager");
        checkCar(car);
        if(car.getId() == null){
            log.log(Level.SEVERE, "CarException : Car id is null");
            throw new CarException("Car id is null");
        }

        if(car.getId() < 0){
            log.log(Level.SEVERE, "CarException : Car id is negative");
            throw new CarException("Car id is negative");
        }

        if(getCarById(car.getId()) == null){
            log.log(Level.SEVERE, "CarException : There is no car with id: " + car.getId() + " in DB");
            throw new CarException("There is no car with id: " + car.getId() + " in DB");
        }

        try {
            String xQuery = "let $doc := doc($document)" +
                    "return update replace $doc/cars/car[@id=$id] with " +
                    "element car{ " +
                    "attribute id {$id}, " +
                    "element manufacturer {$manufacturer}, " +
                    "element km {$km}, " +
                    "element price {$price}, " +
                    "element color {$color}, " +
                    "element description {$description}}";

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");

            service.declareVariable("document", "/db/cars/cars.xml");
            DBUtils.bindCarToXQuery(car, service);

            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            service.execute(compiled);
            log.log(Level.INFO, "Update car is ok");

        } catch (XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while updating car", ex);
        }
    }

    @Override
    public void deleteCar(Car car) {
        log.log(Level.INFO, "Delete car "+car+" in car manager");

        if(car == null){
            log.log(Level.SEVERE, "CarException : Car is null");
            throw new CarException("Car is null");
        }
        if(car.getId() == null){
            log.log(Level.SEVERE, "CarException : Car id is null");
            throw new CarException("Car id is null");
        }

        if(car.getId() < 0){
            log.log(Level.SEVERE, "CarException : Car id is negative");
            throw new CarException("Car id is negative");
        }

        if(getCarById(car.getId()) == null){
            log.log(Level.SEVERE, "CarException : There is no car with id: " + car.getId() + " in DB");
            throw new CarException("There is no car with id: " + car.getId() + " in DB");
        }

        try {
            String xQuery = "let $doc := doc($document)" +
                    "return update delete $doc/cars/car[@id=$id]";

            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");

            service.declareVariable("document", "/db/cars/cars.xml");
            DBUtils.bindCarToXQuery(car, service);

            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            service.execute(compiled);
            log.log(Level.INFO, "Delete car is ok");
        } catch (XMLDBException ex){
            log.log(Level.SEVERE, "XMLDBException:"+ex);
            throw new DBException("Error while deleting car", ex);
        }
    }
}

package PB138.project.database;

import org.exist.xmldb.XQueryService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Michal on 24.4.2015.
 */
public class CarManagerImpl implements CarManager {

    private org.xmldb.api.base.Collection collection;

    public CarManagerImpl(org.xmldb.api.base.Collection collection) {
        this.collection = collection;
    }

    @Override
    public void createCar(Car car) {
        checkCar(car);
        if(car.getId() != null){
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
        }catch(XMLDBException ex){
            throw new DBException("Error while creating new car",ex);
        }

        DBUtils.incrementId(collection, car.getId());
    }

    private void checkCar(Car car){
        if(car == null){
            throw new CarException("Car is null");
        }

        if (car.getManufacturer() == null) {
            throw new CarException("Car manufacturer is null");
        }

        if (car.getManufacturer().isEmpty()) {
            throw new CarException("Car manufacturer is empty");
        }

        if(car.getKm() < 0){
            throw new CarException("Car km is less than zero");
        }

        if(car.getPrice() == null){
            throw new CarException("Car price is null");
        }

        if(car.getPrice().compareTo(new BigDecimal(0)) < 0){
            throw new CarException("Car price is negative");
        }

        if(car.getColor() == null){
            throw new CarException("Car color is null");
        }

        if(car.getColor().isEmpty()){
            throw new CarException("Car color is empty");
        }
    }

    @Override
    public Car getCarById(Long id) {
        if(id == null){
            throw new CarException("id is null");
        }
        if(id < 0){
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
                    throw new CarException("More car with same id");
                }
                return result;
            }
        }catch(XMLDBException ex){
            throw new DBException("Error while creating new car",ex);
        }
        return null;
    }

    @Override
    public Collection<Car> getAllCars() {
        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection);
        }catch(XMLDBException ex){
            throw new DBException("Error while getting all cars", ex);
        }
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByManufacturer(String manufacturer) {
        if(manufacturer == null){
            throw new IllegalArgumentException("manufacturer is null");
        }

        if(manufacturer.isEmpty()){
            throw new IllegalArgumentException("manufacturer is empty");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "manufacturer=$argument0", new String[]{manufacturer});
        }catch(XMLDBException ex){
            throw new DBException("Error while getting cars by manufacturer", ex);
        }
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByColor(String color) {
        if(color== null){
            throw new IllegalArgumentException("color is null");
        }

        if(color.isEmpty()){
            throw new IllegalArgumentException("color is empty");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "color=$argument0", new String[]{color});
        }catch(XMLDBException ex){
            throw new DBException("Error while getting cars by color", ex);
        }
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByKmLessThan(int km) {
        if(km < 0){
            throw new IllegalArgumentException("km is negative");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "km<$argument0", new String[]{String.valueOf(km)});
        }catch(XMLDBException ex){
            throw new DBException("Error while getting cars less km", ex);
        }
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByKmMoreThan(int km) {
        if(km < 0){
            throw new IllegalArgumentException("km is negative");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "km>$argument0", new String[]{String.valueOf(km)});
        }catch(XMLDBException ex){
            throw new DBException("Error while getting cars less km", ex);
        }
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByKm(int from, int to) {
        if(from < 0){
            throw new IllegalArgumentException("from is negative");
        }
        if(to < 0){
            throw new IllegalArgumentException("to is negative");
        }
        if(to < from){
            throw new IllegalArgumentException("to is less than from");
        }

        Collection<Car> resultList;
        try {
            resultList = DBUtils.selectCarsFromDBWhere(collection, "km>=number($argument0) and km<=number($argument1)", new String[]{String.valueOf(from), String.valueOf(to)});
        }catch(XMLDBException ex){
            throw new DBException("Error while getting cars less km", ex);
        }
        return resultList;
    }

    @Override
    public Collection<Car> getCarsBySearchEngine(SearchEngine searchEngine) {
        throw new UnsupportedOperationException("not implemented yet!");
    }

    @Override
    public void updateCar(Car car) {
        checkCar(car);
        if(car.getId() == null){
            throw new CarException("Car id is null");
        }

        if(car.getId() < 0){
            throw new CarException("Car id is negative");
        }

        if(getCarById(car.getId()) == null){
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
        } catch (XMLDBException ex){
            throw new DBException("Error while updating car", ex);
        }
    }

    @Override
    public void deleteCar(Car car) {
        if(car == null){
            throw new CarException("Car is null");
        }
        if(car.getId() == null){
            throw new CarException("Car id is null");
        }

        if(car.getId() < 0){
            throw new CarException("Car id is negative");
        }

        if(getCarById(car.getId()) == null){
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
        } catch (XMLDBException ex){
            throw new DBException("Error while deleting car", ex);
        }
    }
}

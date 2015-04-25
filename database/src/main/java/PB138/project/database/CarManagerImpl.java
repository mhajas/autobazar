package PB138.project.database;

import org.exist.xmldb.XQueryService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.base.*;

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
        car.setId(getNextId());
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
            //System.out.println(car.toXML());
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");

            service.declareVariable("document", "/db/cars/cars.xml");
            bindCarToXQuery(car, service);

            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            service.execute(compiled);
        }catch(XMLDBException ex){
            throw new DBException("Error while creating new car",ex);
        }

        incrementId(car.getId());
    }

    private Long getNextId(){
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

    private void incrementId(Long id) {
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
            //System.out.println(car.toXML());
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");

            service.declareVariable("document", "/db/cars/cars.xml");
            service.declareVariable("id", id);

            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            ResourceSet res = service.execute(compiled);
            ResourceIterator it = res.getIterator();
            if(it.hasMoreResources()){
                Resource resource = it.nextResource();
                Car result = parseCarFromXML(resource.getContent().toString());
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

    private Car parseCarFromXML(String xml){
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
        } catch (ParserConfigurationException e1) {
            // handle ParserConfigurationException
        }
        return null;
    }

    public void bindCarToXQuery(Car car, XQueryService service){
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


    @Override
    public Collection<Car> getAllCars() {
        List<Car> resultList = new ArrayList<>();
        try {
            String xQuery = "let $doc := doc($document)" +
                    "return $doc/cars/car";
            //System.out.println(car.toXML());
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");

            service.declareVariable("document", "/db/cars/cars.xml");

            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            ResourceSet res = service.execute(compiled);
            ResourceIterator it = res.getIterator();
            while(it.hasMoreResources()){
                Resource resource = it.nextResource();
                resultList.add(parseCarFromXML(resource.getContent().toString()));
            }
        }catch(XMLDBException ex){
            throw new DBException("Error while creating new car",ex);
        }
        System.out.println(resultList);
        return resultList;
    }

    @Override
    public Collection<Car> getCarsByManufacturer(String manufacturer) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Car> getCarsByColor(String color) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Car> getCarsByKmLessThan(int km) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Car> getCarsByKmMoreThan(int km) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Car> getCarsByKm(int from, int to) {
        throw new UnsupportedOperationException("not implemented yet");
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
            bindCarToXQuery(car, service);

            service.setProperty("indent", "yes");
            CompiledExpression compiled = service.compile(xQuery);

            service.execute(compiled);
        } catch (XMLDBException ex){
            throw new DBException("Error while updating car", ex);
        }
    }

    @Override
    public void deleteCar(Car car) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}

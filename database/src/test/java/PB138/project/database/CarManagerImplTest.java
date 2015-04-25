package PB138.project.database;

import org.exist.xmldb.XQueryService;
import org.junit.*;
import org.junit.Test;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CarManagerImplTest {

    private CarManagerImpl manager;

    private Collection collection;

    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    private static final String PREFIX = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";
    @Before
    public void setUp() throws Exception {
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
            resource.setContent("<cars></cars>");
            collection.storeResource(resource);

            resource = (XMLResource)collection.createResource("data.xml", "XMLResource");
            resource.setContent("<data><car-next-id>1</car-next-id></data>");
            collection.storeResource(resource);


        } catch (XMLDBException e) {
            System.err.println("XML:DB Exception occurred " + e.errorCode + " " + e.getMessage());
        }

        manager = new CarManagerImpl(collection);
    }

    @After
    public void tearDown() throws Exception {
        Collection parent = DatabaseManager.getCollection(PREFIX,"admin","test123");
        CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");

        mgt.removeCollection(PREFIX + "cars");
        parent.close();
        collection.close();
    }

    @Test
    public void testCreateCar() throws Exception {
        Car testCar = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");

        manager.createCar(testCar);
        Long carId = testCar.getId();
        assertNotNull(carId);

        Car result = manager.getCarById(carId);

        assertEquals(testCar, result);
        assertNotSame(testCar, result);
        assertDeepEquals(testCar, result);

        Car testCar2 = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");

        manager.createCar(testCar2);
        carId = testCar2.getId();
        assertNotNull(carId);

        Car result2 = manager.getCarById(carId);
        assertEquals(testCar2, result2);
        assertNotSame(testCar2, result2);
        assertDeepEquals(testCar2, result2);

        assertNotEquals(result, result2);
        assertDeepNotEquals(result, result2);

    }

    @Test
    public void testCreateCarWithWrongArguments() throws Exception{
        try{
            manager.createCar(null);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            Car testCar=new Car(null ,80000,  new BigDecimal("150000"), "blue", "OK stav");
            manager.createCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            Car testCar = new Car("", 80000, new BigDecimal("150000"), "blue", "OK stav");
            manager.createCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            Car testCar=new Car("Honda" ,-80000,  new BigDecimal("150000"), "blue", "OK stav");
            manager.createCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }
        try{
            Car testCar=new Car("Honda" ,80000,  new BigDecimal("-150000"), "blue", "OK stav");
            manager.createCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            Car testCar = new Car("Honda", 80000, null, "blue", "OK stav");
            manager.createCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            Car testCar = new Car("Honda", 80000, new BigDecimal("150000"), "", "OK stav");
            manager.createCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }
        try{
            Car testCar = new Car("Honda", 80000, new BigDecimal("150000"), null, "OK stav");
            manager.createCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }
    }

    /*
        @Test
        public void testGetCarById() throws Exception {

        }

        @Test
        public void testGetAllCars() throws Exception {

        }

        @Test
        public void testGetCarsByManufacturer() throws Exception {

        }

        @Test
        public void testGetCarsByColor() throws Exception {

        }

        @Test
        public void testGetCarsByKmLessThan() throws Exception {

        }

        @Test
        public void testGetCarsByKmMoreThan() throws Exception {

        }

        @Test
        public void testGetCarsByKm() throws Exception {

        }

        @Test
        public void testUpdateCar() throws Exception {

        }

        @Test
        public void testDeleteCar() throws Exception {

        }*/

    private void assertDeepNotEquals(Car expected, Car actual) {
        boolean res = true;

        if (!expected.getId().equals(actual.getId())) res = false;
        if (expected.getManufacturer().equals(actual.getManufacturer())) res = false;
        if (expected.getKm() != actual.getKm()) res = false;
        if (!expected.getPrice().equals(actual.getPrice()))res = false;
        if (!expected.getColor().equals(actual.getColor())) res = false;
        if (!expected.getDescription().equals(actual.getDescription())) res = false;

        assertFalse(res);
    }

    private void assertDeepEquals(Car expected, Car actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getManufacturer(), actual.getManufacturer());
        assertEquals(expected.getKm(), actual.getKm());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getColor(), actual.getColor());
        assertEquals(expected.getDescription(), actual.getDescription());
    }


}
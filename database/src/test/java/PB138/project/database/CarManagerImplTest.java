package PB138.project.database;

import org.junit.*;
import org.junit.Test;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

public class CarManagerImplTest {

    private CarManagerImpl manager;

    private Collection collection;

    @Before
    public void setUp() throws Exception {
        collection = DBUtils.loadOrCreateCarCollection();
        manager = new CarManagerImpl(collection);
    }

    @After
    public void tearDown() throws Exception {
        DBUtils.dropCarDatabase();
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


    @Test
    public void testGetCarById() throws Exception {
        Car testCar = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
        Car testCar2 = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");
        manager.createCar(testCar);
        manager.createCar(testCar2);
        Long carId=testCar.getId();
        assertNotNull(carId);
        Car result =manager.getCarById(carId);
        assertEquals(testCar,result);
        assertDeepEquals(testCar,result);

        Long carId2=testCar2.getId();
        assertNotNull(carId2);
        Car result2=manager.getCarById(carId2);
        assertEquals(testCar2,result2);
        assertDeepNotEquals(testCar2,result2);
        assertNotEquals(carId,carId2);

    }

    @Test
    public void testGetCarByIdWithWrongArguments() throws Exception {
        try{
            manager.getCarById(null);
            fail();
        }catch (CarException ex) {
            //OK
        }

        try{
            manager.getCarById(-1l);
            fail();
        }catch (CarException ex){
            //OK
        }
    }

    @Test
    public void testGetAllCars() throws Exception {
        Car testCar = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
        Car testCar2 = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");
        Car testCar3 = new Car("Porshe", 30000, new BigDecimal("900000"), "white", "Nove");
        manager.createCar(testCar);
        manager.createCar(testCar2);
        manager.createCar(testCar3);

        List<Car> expected = Arrays.asList(testCar, testCar2, testCar3);
        List<Car> actual = new ArrayList<>(manager.getAllCars());

        Collections.sort(expected, idComparator);
        Collections.sort(actual, idComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);

    }

    @Test
    public void testGetCarsByManufacturer() throws Exception {
        Car testCar = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");
        Car testCar2 = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
        Car testCar3 = new Car("Honda", 30000, new BigDecimal("900000"), "white", "Nove");

        manager.createCar(testCar);
        manager.createCar(testCar2);
        manager.createCar(testCar3);

        List<Car> expected = Arrays.asList(testCar);
        List<Car> actual = new ArrayList<>(manager.getCarsByManufacturer("Ferrari"));
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);

        expected = Arrays.asList(testCar2,testCar3);
        actual = new ArrayList<>(manager.getCarsByManufacturer("Honda"));
        assertEquals(2, actual.size());

        assertNotEquals(actual.get(0), actual.get(1));
        assertDeepNotEquals(actual.get(0), actual.get(1));

        Collections.sort(actual, idComparator);
        Collections.sort(expected, idComparator);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }

    @Test
    public void testGetCarsByManufacturerWithWrongArguments() throws Exception{
        try {
            manager.getCarsByManufacturer(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            manager.getCarsByManufacturer("");
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }

        @Test
        public void testGetCarsByColor() throws Exception {
            Car testCar = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");
            Car testCar2 = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
            Car testCar3 = new Car("Honda", 30000, new BigDecimal("900000"), "blue", "Nove");

            manager.createCar(testCar);
            manager.createCar(testCar2);
            manager.createCar(testCar3);

            List<Car> expected = Arrays.asList(testCar);
            List<Car> actual = new ArrayList<>(manager.getCarsByColor("red"));
            assertEquals(1, actual.size());
            assertEquals(expected, actual);
            assertDeepEquals(expected, actual);

            expected = Arrays.asList(testCar2,testCar3);
            actual = new ArrayList<>(manager.getCarsByColor("blue"));
            assertEquals(2, actual.size());

            assertNotEquals(actual.get(0), actual.get(1));
            assertDeepNotEquals(actual.get(0), actual.get(1));

            Collections.sort(actual, idComparator);
            Collections.sort(expected, idComparator);

            assertEquals(expected, actual);
            assertDeepEquals(expected, actual);
        }

        @Test
        public void testGetCarsByColorWithWrongArguments() throws Exception{
            try {
                manager.getCarsByColor(null);
                fail();
            } catch (IllegalArgumentException ex) {
                //OK
            }

            try {
                manager.getCarsByColor("");
                fail();
            } catch (IllegalArgumentException ex) {
                //OK
            }
        }

        @Test
        public void testGetCarsByKmLessThan() throws Exception {
            Car testCar = new Car("Ferrari", 30000, new BigDecimal("1500000"), "red", "NOK stav");
            Car testCar2 = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
            Car testCar3 = new Car("Honda", 90000, new BigDecimal("900000"), "blue", "Nove");
            manager.createCar(testCar);
            manager.createCar(testCar2);
            manager.createCar(testCar3);

            List<Car> actual = new ArrayList<>(manager.getCarsByKmLessThan(40000));
            List<Car> expectedList = Arrays.asList(testCar);
            assertEquals(1, actual.size());
            assertEquals(expectedList, actual);
            assertDeepEquals(expectedList, actual);

            expectedList = Arrays.asList(testCar,testCar2);
            actual = new ArrayList<>(manager.getCarsByKmLessThan(85000));
            assertEquals(2, actual.size());

            assertNotEquals(actual.get(0), actual.get(1));
            assertDeepNotEquals(actual.get(0), actual.get(1));

            Collections.sort(expectedList, idComparator);
            Collections.sort(actual, idComparator);

            assertEquals(expectedList, actual);
            assertDeepEquals(expectedList, actual);
        }

        @Test
        public void testGetCarsByKmLessThanWithWrongArguments() throws Exception{
            try {
                manager.getCarsByKmLessThan(-1);
                fail();
            } catch (IllegalArgumentException ex) {
                //OK
            }
        }

        @Test
        public void testGetCarsByKmMoreThan() throws Exception {
            Car testCar = new Car("Ferrari", 30000, new BigDecimal("1500000"), "red", "NOK stav");
            Car testCar2 = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
            Car testCar3 = new Car("Honda", 90000, new BigDecimal("900000"), "blue", "Nove");
            manager.createCar(testCar);
            manager.createCar(testCar2);
            manager.createCar(testCar3);

            List<Car> actual = new ArrayList<>(manager.getCarsByKmMoreThan(85000));
            List<Car> expectedList = Arrays.asList(testCar3);
            assertEquals(1, actual.size());
            assertEquals(expectedList, actual);
            assertDeepEquals(expectedList, actual);

            expectedList = Arrays.asList(testCar2,testCar3);
            actual = new ArrayList<>(manager.getCarsByKmMoreThan(35000));
            assertEquals(2, actual.size());

            assertNotEquals(actual.get(0), actual.get(1));
            assertDeepNotEquals(actual.get(0), actual.get(1));

            Collections.sort(expectedList, idComparator);
            Collections.sort(actual, idComparator);

            assertEquals(expectedList, actual);
            assertDeepEquals(expectedList, actual);
        }

    @Test
    public void testGetCarsByKmMoreThanWithWrongArguments() throws Exception{
        try {
            manager.getCarsByKmMoreThan(-10);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }

    @Test
    public void testGetCarsByKm() throws Exception {
        Car testCar = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");
        Car testCar2 = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
        Car testCar3 = new Car("Honda", 80000, new BigDecimal("900000"), "blue", "Nove");
        manager.createCar(testCar);
        manager.createCar(testCar2);
        manager.createCar(testCar3);

        List<Car> actual = new ArrayList<>(manager.getCarsByKm(100000, 1000000000));
        List<Car> expectedList = Arrays.asList(testCar);
        assertEquals(1, actual.size());
        assertEquals(expectedList, actual);
        assertDeepEquals(expectedList, actual);

        expectedList = Arrays.asList(testCar2,testCar3);
        actual = new ArrayList<>(manager.getCarsByKm(0, 99999));
        assertEquals(2, actual.size());

        assertNotEquals(actual.get(0), actual.get(1));
        assertDeepNotEquals(actual.get(0), actual.get(1));

        Collections.sort(expectedList, idComparator);
        Collections.sort(actual, idComparator);

        assertEquals(expectedList, actual);
        assertDeepEquals(expectedList, actual);
    }

    @Test
    public void testGetCarsByKmWithWrongArguments() throws Exception{
        try {
            manager.getCarsByKm(-10, 10);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            manager.getCarsByKm(10, -10);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }

    @Test
    public void testUpdateCar() throws Exception {
        Car testCar = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
        manager.createCar(testCar);
        Long carId = testCar.getId();
        Car testCar2 = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");
        testCar2.setId(carId);
        manager.updateCar(testCar2);
        Car result=manager.getCarById(carId);
        assertEquals(testCar2, result);

    }

    @Test
    public void testUpdateCarWithWrongArguments() throws Exception {
        Car testCar = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
        manager.createCar(testCar);
        Long carId = testCar.getId();

        try{
            manager.updateCar(null);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar.setId(null);
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar=manager.getCarById(carId);
            testCar.setId(null);
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar=manager.getCarById(carId);
            testCar.setManufacturer(null);
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar=manager.getCarById(carId);
            testCar.setManufacturer("");
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar=manager.getCarById(carId);
            testCar.setKm(-1);
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar=manager.getCarById(carId);
            testCar.setPrice(null);
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar=manager.getCarById(carId);
            testCar.setPrice(new BigDecimal("-150000"));
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar=manager.getCarById(carId);
            testCar.setColor(null);
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }

        try{
            testCar=manager.getCarById(carId);
            testCar.setColor("");
            manager.updateCar(testCar);
            fail();
        }catch (CarException ex){
            //OK
        }
    }


        @Test
        public void testDeleteCar() throws Exception {
            Car testCar = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");
            Car testCar2 = new Car("Honda", 80000, new BigDecimal("150000"), "blue", "OK stav");
            manager.createCar(testCar);
            manager.createCar(testCar2);

            manager.deleteCar(testCar);

            assertNull(manager.getCarById(testCar.getId()));
            assertNotNull(manager.getCarById(testCar2.getId()));
        }

    @Test
    public void testDeleteCarWithWrongArgument() throws Exception{
        Car testCar = new Car("Ferrari", 180000, new BigDecimal("1500000"), "red", "NOK stav");
        manager.createCar(testCar);

        try {
            manager.deleteCar(null);
            fail();
        } catch (CarException ex) {
            //OK
        }

        try {
            testCar.setId(null);
            manager.deleteCar(null);
            fail();
        } catch (CarException ex) {
            //OK
        }

        try {
            testCar.setId(1l);
            manager.deleteCar(null);
            fail();
        } catch (CarException ex) {
            //OK
        }
    }

    @Test
    public void testSearchEngine()throws Exception{
        Car testCar = new Car("Ferrari", 30000, new BigDecimal("1500000"), "red", "NOK stav");
        Car testCar2 = new Car("Honda", 50000, new BigDecimal("150000"), "blue", "OK stav");
        Car testCar3 = new Car("Honda", 90000, new BigDecimal("900000"), "blue", "Nove");
        Car testCar4 = new Car("Porshe", 900000, new BigDecimal("900000"), "white", "Nove");

        manager.createCar(testCar);
        manager.createCar(testCar2);
        manager.createCar(testCar3);
        manager.createCar(testCar4);

        SearchEngine a=new SearchEngine();
        a.addCondition("km<{value}", String.valueOf(50000));
        List<Car> actual = new ArrayList<>(manager.getCarsBySearchEngine(a));
        List<Car> expectedList = Arrays.asList(testCar);

        assertEquals(1, actual.size());
        assertEquals(expectedList, actual);
        assertDeepEquals(expectedList, actual);


        a.addCondition("manufacturer={value}","Honda")
                .addCondition("km={value}", String.valueOf(90000))
                .addCondition("price={value}", String.valueOf("900000"))
                .addCondition("color={value}", "blue")
                .addCondition("description={value}", "Nove");
        actual = new ArrayList<>(manager.getCarsBySearchEngine(a));
        expectedList = Arrays.asList(testCar3);

        assertEquals(1, actual.size());
        assertEquals(expectedList, actual);
        assertDeepEquals(expectedList, actual);
        assertNotEquals(actual.get(0), actual.get(1));
        assertDeepNotEquals(actual.get(0), actual.get(1));

        a.addCondition("manufacturer={value}","Honda")
                .addCondition("color={value}","blue");
        actual = new ArrayList<>(manager.getCarsBySearchEngine(a));
        expectedList = Arrays.asList(testCar2,testCar3);

        assertEquals(1, actual.size());
        assertEquals(expectedList, actual);
        assertDeepEquals(expectedList, actual);

    }

    @Test
    public void testSearchEngineWithWrongArgument()throws Exception{
        Car testCar = new Car("Honda", 90000, new BigDecimal("900000"), "blue", "Nove");
        manager.createCar(testCar);
        SearchEngine a=new SearchEngine();

        try {
            a.addCondition("manufacturer={value}", "null");
            manager.getCarsBySearchEngine(a);
            fail();
        } catch (CarException ex) {
            //OK
        }

        try {
            a.addCondition("km={value}", null);
            manager.getCarsBySearchEngine(a);
            fail();
        } catch (CarException ex) {
            //OK
        }

        try {
            a.addCondition("price={value}", null);
            manager.getCarsBySearchEngine(a);
            fail();
        } catch (CarException ex) {
            //OK
        }

        try {
            a.addCondition("color={value}", null);
            manager.getCarsBySearchEngine(a);
            fail();
        } catch (CarException ex) {
            //OK
        }

        try {
            a.addCondition("description={value}", null);
            manager.getCarsBySearchEngine(a);
            fail();
        } catch (CarException ex) {
            //OK
        }

        try {
            SearchEngine b=new SearchEngine();
            manager.getCarsBySearchEngine(b);
            fail();
        } catch (CarException ex) {
            //OK
        }
    }

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

    private void assertDeepEquals(List<Car> expectedList, List<Car> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Car expected = expectedList.get(i);
            Car actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private static Comparator<Car> idComparator = new Comparator<Car>() {

        @Override
        public int compare(Car o1, Car o2) {
            return o1.getId().compareTo(o2.getId());
        }
    };

}
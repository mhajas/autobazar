package PB138.project.database;

import java.util.Collection;

/**
 * Created by Michal on 24.4.2015.
 */
public interface CarManager {

    void createCar(Car car);

    Car getCarById(Long id);

    Collection<Car> getAllCars();

    Collection<Car> getCarsByManufacturer(String manufacturer);

    Collection<Car> getCarsByColor(String color);

    Collection<Car> getCarsByKmLessThan(int km);

    Collection<Car> getCarsByKmMoreThan(int km);

    Collection<Car> getCarsByKm(int from, int to);

    Collection<Car> getCarsBySearchEngine(SearchEngine searchEngine);

    void updateCar(Car car);

    void deleteCar(Car car);


}

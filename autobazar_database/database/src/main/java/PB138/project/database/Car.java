package PB138.project.database;

import org.exist.xmldb.XQueryService;
import org.xmldb.api.base.XMLDBException;

import java.math.BigDecimal;

/**
 * Created by Michal on 24.4.2015.
 */
public class Car {
    private Long id;
    private String manufacturer;
    private int km;
    private BigDecimal price;
    private String color;
    private String description;

    public Car() {}

    public Car(String manufacturer, int km, BigDecimal price, String color, String description) {
        this.manufacturer = manufacturer;
        this.km = km;
        this.price = price;
        this.color = color;
        this.description = description;
    }

    public Car(String manufacturer,  int km, BigDecimal price, String color) {
        this(manufacturer,km,price,color,null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getKm() {
        return km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", km=" + km +
                ", price=" + price +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (km != car.km) return false;
        if (color != null ? !color.equals(car.color) : car.color != null) return false;
        if (manufacturer != null ? !manufacturer.equals(car.manufacturer) : car.manufacturer != null) return false;
        if (id != null ? !id.equals(car.id) : car.id != null) return false;
        if (price != null ? !price.equals(car.price) : car.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (manufacturer != null ? manufacturer.hashCode() : 0);
        result = 31 * result + km;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }

    public String toXML() {
        String result =  "<car id=\"" + getId() + "\">" +
                "<manufacturer>" + getManufacturer() + "</manufacturer>" +
                "<km>" + getKm() + "</km>" +
                "<price>" + getPrice() + "</price>" +
                "<color>" + getColor() + "</color>";
        if(getDescription() != null){
            result += "<description>" + getDescription() + "</description>";
        }
        result += "</car>";

        return result;
    }
}

package PB138.project.web;

import PB138.project.database.Car;
import PB138.project.database.CarException;
import PB138.project.database.CarManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Servlet for managing books.
 */
@WebServlet(CarServlet.URL_MAPPING + "/*")
public class CarServlet extends HttpServlet {

    private static final String LIST_JSP = "/car.jsp";
    public static final String URL_MAPPING = "/cars";

    private final static Logger log = LoggerFactory.getLogger(CarServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        showCarList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String action = request.getPathInfo();
        switch (action) {
            case "/add":
                String manufacturer = request.getParameter("manufacturer");
                int km;
                try {
                    km = Integer.parseInt(request.getParameter("km"));
                }catch (NumberFormatException ex){
                    request.setAttribute("chyba", "Bad km format");
                    showCarList(request, response);
                    return;
                }
                BigDecimal price = new BigDecimal(request.getParameter("price").replaceAll(",", "")); //TODO dorobit
                String color = request.getParameter("color");
                String description = request.getParameter("description");
                if (manufacturer == null
                        || manufacturer.isEmpty()
                        || km<0
                        || color==null
                        || color.isEmpty()
                        || description==null
                        || description.isEmpty()
                        || price.compareTo(new BigDecimal(0)) < 0
                        ) {
                    request.setAttribute("chyba", "Je nutné vyplnit všechny hodnoty spravne !");
                    showCarList(request, response);
                    return;
                }
                try {
                    Car car = new Car();
                    car.setManufacturer(manufacturer);
                    car.setKm(km);
                    car.setPrice(price);
                    car.setColor(color);
                    car.setDescription(description);

                    getCarManager().createCar(car);
                    log.debug("created {}",car);
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (CarException ex) {
                    log.error("Cannot add car", ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            case "/delete":
                try {
                    Long id = Long.valueOf(request.getParameter("id"));
                    getCarManager().deleteCar(getCarManager().getCarById(id));
                    log.debug("deleted car {}",id);
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (CarException ex) {
                    log.error("Cannot delete car", ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            case "/showUpdate":
                Long id = Long.valueOf(request.getParameter("id"));
                Car car=getCarManager().getCarById(id);
                request.setAttribute("manufacturer", car.getManufacturer());
                request.setAttribute("km", car.getKm());
                request.setAttribute("price", car.getPrice());
                request.setAttribute("color", car.getColor());
                request.setAttribute("description", car.getDescription());
                request.setAttribute("id",id);
                showCarList(request, response);
                return;
            case "/update":
                try {
                    id = Long.parseLong(request.getParameter("id"));
                    manufacturer = request.getParameter("manufacturer");
                    km = Integer.parseInt(request.getParameter("km"));
                    price = new BigDecimal(request.getParameter("price").replaceAll(",", "")); //TODO dorobit
                    color = request.getParameter("color");
                    description = request.getParameter("description");
                    if (manufacturer == null
                            || manufacturer.isEmpty()
                            || km<0
                            || color==null
                            || color.isEmpty()
                            || description==null
                            || description.isEmpty()
                            || price.compareTo(new BigDecimal(0)) < 0
                            ) {
                        request.setAttribute("chyba", "Je nutné vyplnit všechny hodnoty spravne !");
                        showCarList(request, response);
                        break;
                    }
                    car = new Car();
                    car.setId(id);
                    car.setManufacturer(manufacturer);
                    car.setKm(km);
                    car.setPrice(price);
                    car.setColor(color);
                    car.setDescription(description);
                    getCarManager().updateCar(car);
                    log.debug("updated {}",car);
                    response.sendRedirect(request.getContextPath()+URL_MAPPING);
                    return;
                } catch (CarException ex) {
                    log.error("Cannot update car", ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                    return;
                }
            case "/buy":
                //TODO
                return;
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    /**
     * Gets BookManager from ServletContext, where it was stored by {@link StartListener}.
     *
     * @return BookManager instance
     */
    private CarManager getCarManager() {
        return (CarManager) getServletContext().getAttribute("carManager");
    }

    /**
     * Stores the list of books to request attribute "books" and forwards to the JSP to display it.
     */
    private void showCarList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("cars", getCarManager().getAllCars());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (Exception e) {
            log.error("Cannot show customer", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}

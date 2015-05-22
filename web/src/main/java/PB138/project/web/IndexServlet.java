package PB138.project.web;

import PB138.project.database.Car;
import PB138.project.database.CarException;
import PB138.project.database.CarManager;
import PB138.project.database.SearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Matej on 21. 5. 2015.
 */
@WebServlet(IndexServlet.URL_MAPPING + "/*")
public class IndexServlet extends HttpServlet {

    private static final String LIST_JSP = "/index.jsp";
    public static final String URL_MAPPING = "/";

    private final static Logger log = LoggerFactory.getLogger(CarServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        showCarList(request, response,getCarManager().getAllCars());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        try {
            String manufacturer = request.getParameter("manufacturer");
            String kmMore = request.getParameter("kmMore");
            String kmLess = request.getParameter("kmLess");
            String priceMore = request.getParameter("priceMore");
            String priceLess = request.getParameter("priceLess");
            String color = request.getParameter("color");
            if (manufacturer == null
                    && kmMore == null
                    && kmLess == null
                    && priceMore == null
                    && priceLess == null
                    && color == null
                    ) {
                showCarList(request, response,getCarManager().getAllCars());
                return;
            }
            SearchEngine search = new SearchEngine();
            if (!(manufacturer == null
                    || manufacturer.isEmpty())) {
                search.addCondition("manufacturer={value}", manufacturer);
            }
            if (!(kmLess == null
                    || kmLess.isEmpty())) {
                search.addCondition("km<{value}", kmLess);
            }
            if (!(kmMore == null
                    || kmMore.isEmpty())) {
                search.addCondition("km>{value}", kmMore);
            }
            if (!(priceLess == null
                    || priceLess.isEmpty())) {
                search.addCondition("price<{value}", priceLess);
            }
            if (!(priceMore == null
                    || priceMore.isEmpty())) {
                search.addCondition("price>{value}", priceMore);
            }
            if (!(color == null
                    || color.isEmpty())) {
                search.addCondition("color={value}", color);
            }
            List<Car> actual = new ArrayList<>(getCarManager().getCarsBySearchEngine(search));
            log.debug("filter {}");
            showCarList(request,response,actual);
            return;
        } catch (CarException ex) {
            log.error("Cannot show car", ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
            return;
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
    private void showCarList(HttpServletRequest request, HttpServletResponse response,Collection<Car> cars) throws ServletException, IOException {
        try {
            request.setAttribute("cars", cars);
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (Exception e) {
            log.error("Cannot show customer", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
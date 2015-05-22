package PB138.project.web;

import PB138.project.database.Car;
import PB138.project.database.CarBillTransformation;
import PB138.project.database.CarManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Matej on 21. 5. 2015.
 */

@WebServlet(BuyServlet.URL_MAPPING + "/*")
public class BuyServlet extends HttpServlet {

    private static final String LIST_JSP = "/buy.jsp";
    public static final String URL_MAPPING = "/buy";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
            case "/sell":
                createContract(request, response, getCarManager().getCarById(Long.parseLong(request.getParameter("carId"))));
                try {
                    CarBillTransformation.makeBill(getServletContext().getRealPath("\\"));
                }catch(TransformerException ex){

                }
                //getCarManager().deleteCar(getCarManager().getCarById(Long.parseLong(request.getParameter("carId"))));
                log.debug("buy car {}", request.getParameter("carId"));
                request.getRequestDispatcher("/car.html").forward(request, response);
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
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("car", getCarManager().getCarById(id));
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (Exception e) {
            log.error("Cannot show car", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    private void createContract(HttpServletRequest request, HttpServletResponse response, Car car) throws ServletException{

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getServletContext().getRealPath("\\WEB-INF\\classes\\tmp\\contract.xml")))) {
            bw.write("<contract>");
            bw.newLine();
            bw.write("<car>");
            bw.newLine();
            bw.write("<manufacturer>");
            bw.write(car.getManufacturer());
            bw.write("</manufacturer>");
            bw.newLine();
            bw.write("<km>");
            bw.write(String.valueOf(car.getKm()));
            bw.write("</km>");
            bw.newLine();
            bw.write("<price>");
            bw.write(car.getPrice().toString());
            bw.write("</price>");
            bw.newLine();
            bw.write("<color>");
            bw.write(car.getColor());
            bw.write("</color>");
            bw.newLine();
            bw.write("<description>");
            bw.write(car.getDescription());
            bw.write("</description>");
            bw.newLine();
            bw.write("</car>");
            bw.newLine();
            bw.write("<customer>");
            bw.newLine();
            bw.write("<name>");
            bw.write(request.getParameter("name"));
            bw.write("</name>");
            bw.newLine();
            bw.write("<adress>");
            bw.write(request.getParameter("adress"));
            bw.write("</adress>");
            bw.newLine();
            bw.write("<dateOfBirth>");
            bw.write(new SimpleDateFormat("dd-MM-yyyy").format(sdf.parse(request.getParameter("born").replace("T", " "))));
            bw.write("</dateOfBirth>");
            bw.newLine();
            bw.write("</customer>");
            bw.newLine();
            bw.write("</contract>");
            bw.flush();
        }
        catch (ParseException e){
            System.out.println("Nepodarilo sa rozparsovat datum");
        }
        catch(IOException e)
        {
            System.err.println("Do souboru se nepovedlo zapsat.\n");
        }
    }
}
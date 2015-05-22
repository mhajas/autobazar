package PB138.project.web;

import PB138.project.database.Car;
import PB138.project.database.CarBillTranformation;
import PB138.project.database.CarManager;
import sun.tools.jar.Main;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;


@WebServlet(BuyServlet.URL_MAPPING + "/*")
public class BuyServlet extends HttpServlet {

    private static final String LIST_JSP = "/buy.jsp";
    public static final String URL_MAPPING = "/buy";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Main.class.getName());

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
                    CarBillTranformation.makeBill(getServletContext().getRealPath("\\"));
                }catch(TransformerException ex){

                }
                getCarManager().deleteCar(getCarManager().getCarById(Long.parseLong(request.getParameter("carId"))));
                log.log(Level.INFO, "Buy car with ID :" + request.getParameter("carId"));
                request.getRequestDispatcher("/car.html").forward(request, response);
                return;
            default:
                log.log(Level.SEVERE, "Unknown action" + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    /**
     * Gets Car Manager from ServletContext, where it was stored by {@link StartListener}.
     *
     * @return BookManager instance
     */
    private CarManager getCarManager() {
        return (CarManager) getServletContext().getAttribute("carManager");
    }


    private void showCarList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("car", getCarManager().getCarById(id));
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Cannot show car" + e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    private void createContract(HttpServletRequest request, HttpServletResponse response, Car car) throws ServletException {

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
            //bw.write(car.getColor());
            String color = car.getColor();
            Color c=hex2Rgb(color);
            bw.write("[R "+c.getRed()+";G "+c.getGreen()+ ";B "+c.getBlue()+"]");
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
        } catch (ParseException e) {
            System.out.println("Nepodarilo sa rozparsovat datum");
        } catch (IOException e) {
            System.err.println("Do souboru se nepovedlo zapsat.\n");
        }
    }
        private Color hex2Rgb(String colorStr) {
            return new Color(
                    Integer.valueOf(colorStr.substring(1, 3), 16),
                    Integer.valueOf(colorStr.substring(3, 5), 16),
                    Integer.valueOf(colorStr.substring(5, 7), 16));
        }
}
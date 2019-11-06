import org.json.simple.JSONObject;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class carsCount extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //connect to db and get count of records
        int count = 0;
        try {
            count = Controllers.countOfCars();
        } catch (SQLException e) {
            System.out.println(e);
        }

    //write response in json format
        PrintWriter writer = response.getWriter();
        JSONObject jsonResponse = new JSONObject();
        response.setContentType("application/json; charset=utf-8");
        try {
            response.setStatus(200);
            jsonResponse.put("registeredCarsCount", count);
            writer.println(jsonResponse);
        } finally {
            writer.close();
        }
    }
}

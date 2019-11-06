import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.*;
import java.time.*;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class registeredCars extends HttpServlet {
    static String reg = "[A-Z0-9\\- ]{4,16}";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        //get request body and parse body to json
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject json = null;
        try {
            json = (JSONObject) new JSONParser().parse(body);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //get values carNumber and timestamp
        String carNumber = json.get("carNumber").toString();
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
//        String timestamp = OffsetDateTime.now().format(dateFormatter);

        //regular expression comparison
        if (!carNumber.matches(reg)) {
            try {
                response.setStatus(400);
                jsonResponse.put("Status", response.getStatus());
                jsonResponse.put("Message", "Invalid data, required format: " + reg);
                writer.println(jsonResponse);
            } finally {
                writer.close();
                return;
            }
        }

        response.setContentType("application/json; charset=utf-8");
        //connect to db and insert row
        try {
            Controllers.addCar(carNumber);
            //write response success
            response.setStatus(200);
            jsonResponse.put("carNumber", carNumber);
            jsonResponse.put("timestamp", LocalDateTime.now().atZone(ZoneId.systemDefault()));
            writer.println(jsonResponse);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //write response server error
            response.setStatus(500);
            jsonResponse.put("Status", response.getStatus());
            jsonResponse.put("Message", "Data Base error:" + e);
            writer.println(jsonResponse);
        } finally {
            writer.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        //get values from request
        String carNumber = request.getParameter("carNumber");
        if(!(carNumber == null)) {
            if (!carNumber.matches(reg)) {
                try {
                    response.setStatus(400);
                    writer.println("HTTP Status " + response.getStatus());
                    writer.println("Invalid data, required format: " + reg);
                } finally {
                    writer.close();
                    return;
                }
            }
        }
        String date = request.getParameter("date");
        ResultSet result = null;

        //parse date from request
        LocalDate dateLocal = Controllers.stringDateToLocalDate(date);
        //filter results
        try {
            result = Controllers.getRecordsByFilters(carNumber, dateLocal);
        } catch (SQLException e) {
            System.out.println(e);
        }

        response.setContentType("application/json; charset=utf-8");
        JSONArray jsonArray = new JSONArray();
        try {
            while (result.next()) {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("carNumber", result.getString("CARNUMBER"));
                jsonItem.put("date", result.getTimestamp("TIMESTAMP"));
                jsonArray.add(jsonItem);
            }
            writer.println(jsonArray);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            writer.close();
        }
    }
}

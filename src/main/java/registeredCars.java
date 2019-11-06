import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.*;

import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Iterator;
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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        String timestamp = OffsetDateTime.now().format(dateFormatter);

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

        //connect to db and insert row
        try {
            Controllers.addCar(carNumber, timestamp);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //write response in json format
        response.setContentType("application/json; charset=utf-8");
        try {
            response.setStatus(200);
            jsonResponse.put("carNumber", carNumber);
            jsonResponse.put("timestamp", timestamp);
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
        ArrayList<String[]> array = new ArrayList<>();

        //connect to db and make filter query
        try {
            Connection conn = DataBase.getDb().connection;
            PreparedStatement preparedSt = null;
            ResultSet result;

            //make query to table
            String q = null;
            if (carNumber == null) {
                q = "SELECT * FROM TEST";
                preparedSt = conn.prepareStatement(q);
            } else {
                q = "SELECT * FROM TEST WHERE CARNUMBER = ? ";
                preparedSt = conn.prepareStatement(q);
                preparedSt.setString(1, carNumber);
            }
            result = preparedSt.executeQuery();

        //parse date from request
            String dataForm = "yyyyMMdd";
            LocalDate dateTime = null;
            if (date != null) {
                //parse date from request to localDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dataForm);

                //regular expression comparison
                try {
                    dateTime = LocalDate.parse(date, formatter);
                } catch (Exception e) {
                    response.setStatus(400);
                    jsonResponse = new JSONObject();
                    jsonResponse.put("HTTPstatus", response.getStatus());
                    jsonResponse.put("Message", "Invalid data, required format: " + dataForm);
                    writer.println(jsonResponse);
                    writer.close();
                    return;
                }
            }

            //filter result by date
            String name = null;
            String time = null;
            while (result.next()) {
                name = result.getString("CARNUMBER");
                time = result.getString("TIMESTAMP");
                //if date is null then add all results to response
                if (date != null) {
                    //parse date in the table to localDate
                    LocalDate carDate = null;
                    try {
                        carDate = ZonedDateTime.parse(time).toLocalDate();
                    } catch (Exception e) {
                        continue;
                    }

                    //compare carDate with request date
                    if (dateTime.compareTo(carDate) == 0) {
                        array.add(new String[]{name, time});
                    }
                } else {
                    array.add(new String[]{name, time});
                }
            }
        } catch (SQLException e) {

        }

//        Controllers.getRecordsByFilters(carNumber, date);

        //write response in json format
        response.setContentType("application/json; charset=utf-8");
        JSONArray jsonArray = new JSONArray();
        try {
            Iterator list = array.iterator();
            while (list.hasNext()) {
                JSONObject jsonItem = new JSONObject();
                String[] str = (String[]) list.next();
                jsonItem.put("carNumber", str[0]);
                jsonItem.put("date", str[1]);
                jsonArray.add(jsonItem);
            }
            writer.println(jsonArray);
        } finally {
            writer.close();
        }
    }
}

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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class registeredCars extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        //regular expression
        String reg = "[A-Z0-9\\- ]{4,16}";

        //get Driver class
        String DB_Driver = "org.h2.Driver";
        try {
            Class.forName(DB_Driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //get request body
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        //parse body to json
        Object json = null;
        try {
            json = new JSONParser().parse(body);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsob = (JSONObject) json;

        //get values carNumber and timestamp
        String carNumber = jsob.get("carNumber").toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        String timestamp = OffsetDateTime.now().format(dtf);

        //regular expression comparison
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

        //connect to db and insert row
        try { //getConnection("jdbc:h2:./test", "sa", " ");
            Connection conn = DriverManager.
                    getConnection("jdbc:h2:mem:test");

            //creat table if not exists
            Statement st = null;
            st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS TEST (ID INT PRIMARY KEY AUTO_INCREMENT, CARNUMBER VARCHAR(16), TIMESTAMP VARCHAR(40))");

            //insert row to table
            PreparedStatement st1 = null;
            String q = "insert into TEST(CARNUMBER, TIMESTAMP ) values(?, ?)";
            st1 = conn.prepareStatement(q);
            st1.setString(1, carNumber);
            st1.setString(2, timestamp);
            st1.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //write response in json format
        response.setContentType("application/json; charset=utf-8");
        try {
            writer.println("{\"carNumber\":\"" + carNumber + "\",");
            writer.println("\"timestamp\":\"" + timestamp + "\"}");
            response.setStatus(200);
        } finally {
            writer.close();
        }
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //regular expression
        String reg = "[A-Z0-9\\- ]{4,16}";

        PrintWriter writer = response.getWriter();

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

        //get Driver class
        String DB_Driver = "org.h2.Driver";
        try {
            Class.forName(DB_Driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //connect to db and make filter query
        try {
            Connection conn = DriverManager.
                    getConnection("jdbc:h2:mem:test");
            PreparedStatement st1 = null;
            ResultSet result;

            //creat table if not exists
            Statement st = null;
            st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS TEST (ID INT PRIMARY KEY AUTO_INCREMENT, CARNUMBER VARCHAR(16), TIMESTAMP VARCHAR(40))");

            //make query to table
            String q = null;
            if (carNumber == null) {
                q = "SELECT * FROM TEST";
                st1 = conn.prepareStatement(q);
            } else {
                q = "SELECT * FROM TEST WHERE CARNUMBER = ? ";
                st1 = conn.prepareStatement(q);
                st1.setString(1, carNumber);
            }
            result = st1.executeQuery();

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
                    writer.println("HTTP Status " + response.getStatus());
                    writer.println("Invalid data, required format: " + dataForm);
                    response.getStatus();
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

        //write response in json format
        response.setContentType("application/json; charset=utf-8");
        try {
            Iterator list = array.iterator();
            writer.println("[");
            while (list.hasNext()) {
                String[] str = (String[]) list.next();
                writer.println("{\"carNumber\":\"" + str[0] + "\",");
                writer.println("\"date\":\"" + str[1] + "\"}");
                if (list.hasNext()) {
                    writer.println(",");
                }
            }
            writer.println("]");
        } finally {
            writer.close();
        }
    }
}

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

public class Controllers {
    //    public static JSONObject regCar(JSONObject body) {
//        //regular expression
//        String reg = "[A-Z0-9\\- ]{4,16}";
//
//        //get request body
//        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
//
//        //parse body to json
//        Object json = null;
//        try {
//            json = new JSONParser().parse(body);
//        } catch (
//                ParseException e) {
//            e.printStackTrace();
//        }
//        JSONObject jsob = (JSONObject) json;
//
//        //get values carNumber and timestamp
//        String carNumber = jsob.get("carNumber").toString();
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
//        String timestamp = OffsetDateTime.now().format(dtf);
//
//        //regular expression comparison
//        if (!carNumber.matches(reg)) {
//            try {
//                response.setStatus(400);
//                writer.println("HTTP Status " + response.getStatus());
//                writer.println("Invalid data, required format: " + reg);
//            } finally {
//                return;
//            }
//        }
//
//        //connect to db and insert row
//        try {
//            Connection conn = DataBase.getDb().connection;
//
//            //insert row to table
//            String q = "insert into TEST(CARNUMBER, TIMESTAMP ) values(?, ?)";
//            PreparedStatement prepareSt = conn.prepareStatement(q);
//            prepareSt.setString(1, carNumber);
//            prepareSt.setString(2, timestamp);
//            prepareSt.execute();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        //write response in json format
//        response.setContentType("application/json; charset=utf-8");
//        try {
//            JSONObject jsonResponse = new JSONObject();
//            jsonResponse.put("carNumber", carNumber);
//            jsonResponse.put("timestamp", timestamp);
//            writer.println(jsonResponse);
//            response.setStatus(200);
//        } finally {
//            writer.close();
//        }
//    }
    public static void addCar(String carNumber, String timestamp) throws SQLException {
        //connect to db and insert row
        Connection conn = DataBase.getDb().connection;

        String q = "insert into TEST(CARNUMBER, TIMESTAMP ) values(?, ?)";
        PreparedStatement preparedSt = conn.prepareStatement(q);
        preparedSt.setString(1, carNumber);
        preparedSt.setString(2, timestamp);
        preparedSt.execute();
    }

    public static int countOfCars() throws SQLException {
        int count = 0;
        Connection conn = DataBase.getDb().connection;
        Statement st = conn.createStatement();

        ResultSet result;
        result = st.executeQuery("SELECT COUNT(*) FROM TEST");
        if(result.next()) {
            count = result.getInt(1);
        }

        System.out.println(count);
        return count;
    }

    public static ResultSet getAllRecords() throws SQLException {
        Connection conn = DataBase.getDb().connection;
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

        ResultSet result = st.executeQuery("SELECT * FROM TEST");

        return result;
    }

    public static ResultSet getRecordsByCarNumber(ResultSet records, String carNumber) throws SQLException {
        Connection conn = DataBase.getDb().connection;
        PreparedStatement preparedSt;
        ResultSet result = null;

        if (carNumber != null) {
            while(records.next()) {
                if (records.getString("CARNUMBER") != carNumber) {
//                    System.out.println(records.getString("CARNUMBER"));
                    records.deleteRow();
                }
            }
        }
//        records.afterLast();
        result = records;
        //make query to table
//        String request;
//        if (carNumber == null) {
//            request = "SELECT * FROM TEST";
//        } else {
//            request = "SELECT * FROM TEST WHERE CARNUMBER = ? ";
//        }
//        preparedSt = conn.prepareStatement(request);
//        if (carNumber != null) preparedSt.setString(1, carNumber);
//        result = preparedSt.executeQuery();
        while(result.previous()) {
            System.out.println(result.getString("CARNUMBER"));
        }

        return result;
    }

//    public static ResultSet getRecordsByTimestamp(String timestamp) throws SQLException {
//        Connection conn = DataBase.getDb().connection;
//        PreparedStatement preparedSt = null;
//        ResultSet result = null;
//
//        //parse date from request
//        String dataForm = "yyyyMMdd";
//        LocalDate dateTime = null;
//        if (timestamp != null) {
//            //parse date from request to localDate
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dataForm);
//
//            //regular expression comparison
//            try {
//                dateTime = LocalDate.parse(timestamp, formatter);
//            } catch (Exception e) {
//
//            }
//        }
//
//        //filter result by date
//        String name = null;
//        String time = null;
//        while (result.next()) {
//            name = result.getString("CARNUMBER");
//            time = result.getString("TIMESTAMP");
//            //if date is null then add all results to response
//            if (timestamp != null) {
//                //parse date in the table to localDate
//                LocalDate carDate = null;
//                try {
//                    carDate = ZonedDateTime.parse(time).toLocalDate();
//                } catch (Exception e) {
//                    continue;
//                }
//
//                //compare carDate with request date
//                if (dateTime.compareTo(carDate) == 0) {
//                    array.add(new String[]{name, time});
//                }
//            } else {
//                array.add(new String[]{name, time});
//            }
//        }
//        return result;
//    }
}

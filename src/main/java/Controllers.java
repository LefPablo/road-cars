import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class Controllers {
    public static void addCar(String carNumber) throws SQLException {
        //connect to db and insert row
        Connection conn = DataBase.getDb().connection;

        String q = "insert into TEST(CARNUMBER) values(?)";
        PreparedStatement preparedSt = conn.prepareStatement(q);
        preparedSt.setString(1, carNumber);
        preparedSt.execute();
    }

    public static void addCar(String carNumber, Timestamp timestamp) throws SQLException {
        //connect to db and insert row
        Connection conn = DataBase.getDb().connection;

        String q = "insert into TEST(CARNUMBER, TIMESTAMP ) values(?, ?)";
        PreparedStatement preparedSt = conn.prepareStatement(q);
        preparedSt.setString(1, carNumber);
        preparedSt.setTimestamp(2, timestamp);
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

        while(result.next()) {
            System.out.println(result.getString("TIMESTAMP"));
        }

        return result;
    }

    public static LocalDate stringDateToLocalDate (String date) {
        String dataFormat = "yyyyMMdd";
        LocalDate dateTime = null;
        if (date != null) {
            //parse date from request to localDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dataFormat);
            //regular expression comparison
            dateTime = LocalDate.parse(date, formatter);
        }
        return dateTime;
    }

    public static ResultSet getRecordsByFilters(String carNumber, LocalDate date) throws SQLException {
        Connection conn = DataBase.getDb().connection;
        PreparedStatement preparedSt = null;
        ResultSet result;

        String q = null;
        if (carNumber == null) {
            if (date == null) {
                q = "SELECT * FROM TEST";
                preparedSt = conn.prepareStatement(q);
            } else {
                q = "SELECT * FROM TEST WHERE DAY(TIMESTAMP)=? AND MONTH(TIMESTAMP)=? AND YEAR(TIMESTAMP)=?";
                preparedSt = conn.prepareStatement(q);
                preparedSt.setString(1, String.valueOf(date.getDayOfMonth()));
                preparedSt.setString(2, String.valueOf(date.getMonthValue()));
                preparedSt.setString(3, String.valueOf(date.getYear()));
            }
        } else {
            if (date == null) {
                q = "SELECT * FROM TEST WHERE CARNUMBER = ? ";
                preparedSt = conn.prepareStatement(q);
                preparedSt.setString(1, carNumber);
            } else {
                q = "SELECT * FROM TEST WHERE CARNUMBER = ? AND DAY(TIMESTAMP)=? AND MONTH(TIMESTAMP)=? AND YEAR(TIMESTAMP)=?";
                preparedSt = conn.prepareStatement(q);
                preparedSt.setString(1, carNumber);
                preparedSt.setString(2, String.valueOf(date.getDayOfMonth()));
                preparedSt.setString(3, String.valueOf(date.getMonthValue()));
                preparedSt.setString(4, String.valueOf(date.getYear()));
            }
        }
        result = preparedSt.executeQuery();

        return result;
    }

}

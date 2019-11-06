import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class ControllersTest {

    @Test
    public void addCar() {
    }

    @Test
    public void countOfCars() {
    }

    @Test
    public void getAllRecords() {
    }

    @Test
    public void getRecordsByFilters() {
        try {
            Controllers.addCar("3423454");
            Controllers.addCar("5623454");
            Controllers.addCar("3423454", Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
            Controllers.getRecordsByFilters("3423454", Controllers.stringDateToLocalDate("20191106"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
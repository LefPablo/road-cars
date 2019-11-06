import org.junit.Test;

import java.sql.SQLException;

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
    public void getRecordsByCarNumber() {
        try {
            Controllers.addCar("3423454","4564565");
            Controllers.addCar("5623454","985565");
            Controllers.countOfCars();
            Controllers.getRecordsByCarNumber(Controllers.getAllRecords(), "3423454");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.*;

public class carsCount extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //get Driver class
        String DB_Driver = "org.h2.Driver";
        try {
            Class.forName(DB_Driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        int count = 0;
    //connect to db and get count of records
        try { //getConnection("jdbc:h2:./test", "sa", " ");
            Connection conn = DriverManager.
                    getConnection("jdbc:h2:mem:test");
            Statement st = null;
            st = conn.createStatement();
            ResultSet result;
            result = st.executeQuery("SELECT COUNT(*) FROM TEST");
            if(result.next()) {
                count = result.getInt(1);
            }
        } catch (SQLException e) {

        }

    //write response in json format
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json; charset=utf-8");
        try {
            writer.println("{\"registeredCarsCount\":\"" + count + "\"}");
        } finally {
            writer.close();
        }
    }
}

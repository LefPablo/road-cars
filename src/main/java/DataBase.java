import java.sql.*;

public final class DataBase {
    private static DataBase db;
    public Connection connection;
    public Statement statement;

    private DataBase () {
        //get Driver class
        String DB_Driver = "org.h2.Driver";
        try {
            Class.forName(DB_Driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //connect to db
        try {   //getConnection("jdbc:h2:./test", "sa", " "); local db
            connection = DriverManager.getConnection("jdbc:h2:mem:test");
            statement = connection.createStatement();
            creatTable();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void creatTable() throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS TEST (ID INT PRIMARY KEY AUTO_INCREMENT, CARNUMBER VARCHAR(16), TIMESTAMP VARCHAR(40))");
    }

    public static synchronized DataBase getDb() {
        if (db == null) {
            synchronized (DataBase.class) {
                if (db == null)
                    db = new DataBase();
            }
        }
        return db;
    }
}

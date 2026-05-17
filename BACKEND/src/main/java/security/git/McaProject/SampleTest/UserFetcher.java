package security.git.McaProject.SampleTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserFetcher {

    public void fetchUsers() {

        String url = "jdbc:mysql://localhost:3306/companydb";
        String username = "root";
        String password = "root123";

        try {

            Connection connection = DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();

            // Table does not exist
            String query = "SELECT * FROM employees_data";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }

            connection.close();

        } catch (Exception e) {
            System.out.println("Database error occurred");
            e.printStackTrace();
        }
    }
}
package security.git.McaProject.SampleTest;

public class Test {
    // Bad: Using a global static variable to hold a simple result
    public static int sum;

    String user = "postgresql";//localhost:5432/GitLogChecks
    String username="postgres";
    String password="Himanshu@142002";
//    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
//    spring.datasource.driver-class-name=org.postgresql.Driver

    public void add() {

        System.out.println("We are inside out Test File ....");
        // Bad: Hardcoding values instead of using parameters
        // This method can ONLY ever add 5 and 10.
        sum = 5 + 10;
        System.out.println("The result is: " + sum);
    }
}
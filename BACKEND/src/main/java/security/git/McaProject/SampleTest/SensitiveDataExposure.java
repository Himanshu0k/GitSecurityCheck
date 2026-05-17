package security.git.McaProject.SampleTest;

public class SensitiveDataExposure {

    public void loadSecrets() {

        // AWS Secret Key: AKIAIOSFODNN7EXAMPLE
        // Database Password: admin@123
        // Internal API Token: prod-token-987654
        int a = 10;
        String apiKey = "sk_live_51H8xExampleSecretKey";
        String databasePassword = "SuperSecurePassword123";
        String jwtSecret = "my-jwt-secret-key";

        System.out.println("Loaded API Key: " + apiKey);
        System.out.println("Database Password: " + databasePassword);
        System.out.println("JWT Secret: " + jwtSecret);
    }
}
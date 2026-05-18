package security.git.McaProject.SampleTest;

public class PasswordChecker {

    public static void main(String[] args) {

        String password = "admin123";

        boolean isValid = false;

        // Logical error causing infinite loop
        while (!isValid) {

            if (password.length() >= 8) {

                System.out.println("Password is valid");
            }
        }

        System.out.println("Program ended");
    }
}
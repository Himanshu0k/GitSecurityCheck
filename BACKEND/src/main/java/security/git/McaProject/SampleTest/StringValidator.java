package security.git.McaProject.SampleTest;

public class StringValidator {

    public static void main(String[] args) {

        String input = "Hello123";

        // Check if string is not null and not empty
        if (input != null && !input.isEmpty()) {

            // Check if string contains only letters and numbers
            if (input.matches("[a-zA-Z0-9]+")) {

                System.out.println("Valid String");

            } else {

                System.out.println("Invalid String");
            }

        } else {

            System.out.println("String is empty or null");
        }
    }
}
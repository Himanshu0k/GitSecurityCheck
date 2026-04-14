package security.git.McaProject.ai;

import org.springframework.stereotype.Component;

@Component
public class ResponseParser {

    public void parse(String response) {

        System.out.println("📊 Gemini Response:");
        System.out.println(response);

        // Next step:
        // - extract JSON
        // - map to Java object
        // - store in DB
    }
}
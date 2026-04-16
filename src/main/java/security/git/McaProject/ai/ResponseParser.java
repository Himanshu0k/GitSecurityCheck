//package security.git.McaProject.ai;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class ResponseParser {
//
//    public void parse(String response) {
//
//        System.out.println("📊 Gemini Response:");
//        System.out.println(response);
//
//        // Next step:
//        // - extract JSON
//        // - map to Java object
//        // - store in DB
//    }
//}

package security.git.McaProject.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ResponseParser {

    public JsonNode parse(String rawResponse) {
        try {
            String cleaned = rawResponse
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(cleaned);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response", e);
        }
    }
}
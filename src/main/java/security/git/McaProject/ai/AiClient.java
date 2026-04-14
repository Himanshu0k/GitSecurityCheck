package security.git.McaProject.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiClient {

    @Value("${gemini.api.key}")
    private static String apiKey;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

    public String callGemini(String prompt) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
        {
          "contents": [{
            "parts": [{
              "text": "%s"
            }]
          }]
        }
        """.formatted(prompt.replace("\"", "\\\""));

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GEMINI_URL + apiKey,
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }
}
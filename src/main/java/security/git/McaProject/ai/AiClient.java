//package security.git.McaProject.ai;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class AiClient {
//
//    @Value("${gemini.api.key}")
//    private String apiKey;
//
////    private final String GEMINI_URL =
////            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
//private final String GEMINI_URL =
//        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
//
//    String url = GEMINI_URL + "?key" + apiKey;
//
//    public String callGemini(String prompt) {
//
//        System.out.println("API: " + apiKey);
////        System.out.println("GEMINI URL: " + GEMINI_URL);
//        System.out.println("GEMINI URL: " + url);
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        String requestBody = """
//        {
//          "contents": [{
//            "parts": [{
//              "text": "%s"
//            }]
//          }]
//        }
//        """.formatted(prompt.replace("\"", "\\\""));
//
//        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                url + apiKey,
//                HttpMethod.POST,
//                request,
//                String.class
//        );
//
//        return response.getBody();
//    }
//}

package security.git.McaProject.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent";

    public String callGemini(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Gemini API key is missing!");
        }

        String url = BASE_URL + "?key=" + apiKey;

        System.out.println("API Key Loaded: true");
        System.out.println("Final URL: " + BASE_URL); // don't print full key in logs

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
                url,
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }
}
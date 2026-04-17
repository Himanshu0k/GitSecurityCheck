//package security.git.McaProject.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import security.git.McaProject.ai.AiClient;
//import security.git.McaProject.ai.PromptBuilder;
//import security.git.McaProject.ai.ResponseParser;
//
//@Service
//public class AnalysisService {
//
//    @Autowired
//    private AiClient aiClient;
//
//    @Autowired
//    private PromptBuilder promptBuilder;
//
//    @Autowired
//    private ResponseParser responseParser;
//
//    @Autowired
//    private GithubApiService githubApiService;
//
//    public void analyzeCode(String code) {
//
//        // ✅ Limit payload size
//        if (code.length() > 6000) {
//            code = code.substring(0, 6000);
//        }
//
//        String prompt = promptBuilder.buildSecurityPrompt(code);
//
//        int maxRetries = 3;
//
//        for (int i = 0; i < maxRetries; i++) {
//            try {
//                System.out.println("🧠 Sending code to Gemini... Attempt " + (i + 1));
//
//                String response = callGeminiWithFallback(prompt);
//
//                // ✅ Parse response (important for your project goal)
//                responseParser.parse(response);
//
//                // ✅ Small delay to avoid rate limiting
//                Thread.sleep(1500);
//
//                return; // success → exit
//
//            } catch (Exception e) {
//                System.out.println("❌ Gemini failed. Retrying...");
//                e.printStackTrace();
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ignored) {}
//            }
//        }
//
//        System.out.println("❌ Gemini failed after all retries");
//    }
//
//    private String callGeminiWithFallback(String prompt) {
//
//        try {
//            // ✅ Try latest model first
//            return aiClient.callGemini(prompt, "gemini-2.5-flash");
//
//        } catch (Exception e) {
//            System.out.println("⚠️ Falling back to gemini-1.5-flash");
//
//            // ✅ Fallback model
//            return aiClient.callGemini(prompt, "gemini-1.5-flash");
//        }
//    }
//}

//package security.git.McaProject.services;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import security.git.McaProject.ai.AiClient;
//import security.git.McaProject.ai.PromptBuilder;
//import security.git.McaProject.ai.ResponseParser;
//
//@Service
//public class AnalysisService {
//
//    @Autowired
//    private AiClient aiClient;
//
//    @Autowired
//    private PromptBuilder promptBuilder;
//
//    @Autowired
//    private ResponseParser responseParser;
//
//    public JsonNode analyzeCode(String code) {
//
//        if (code == null || code.isBlank()) {
//            System.out.println("⚠️ Empty code received, skipping...");
//            return null;
//        }
//
//        // limit payload
//        if (code.length() > 6000) {
//            code = code.substring(0, 6000);
//        }
//
//        String prompt = promptBuilder.buildSecurityPrompt(code);
//
//        int maxRetries = 3;
//
//        for (int i = 0; i < maxRetries; i++) {
//            try {
//                System.out.println("🧠 Gemini Analysis Attempt " + (i + 1));
//
//                String response = callGeminiWithFallback(prompt);
//
//                // parse + return
//                return responseParser.parse(response);
//
//            } catch (Exception e) {
//                System.out.println("❌ Attempt failed: " + (i + 1));
//                e.printStackTrace();
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ignored) {}
//            }
//        }
//
//        throw new RuntimeException("Gemini failed after retries");
//    }
//
//    private String callGeminiWithFallback(String prompt) {
//        try {
//            return aiClient.callGemini(prompt, "gemini-2.5-flash");
//        } catch (Exception e) {
//            System.out.println("⚠️ Falling back to gemini-2.5-flash once more");
////            return aiClient.callGemini(prompt, "gemini-1.5-flash");
//            return aiClient.callGemini(prompt, "gemini-2.5-flash");
//
//        }
//    }
//}

package security.git.McaProject.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import security.git.McaProject.ai.AiClient;
import security.git.McaProject.ai.PromptBuilder;
import security.git.McaProject.ai.ResponseParser;

@Service
public class AnalysisService {

    @Autowired
    private AiClient aiClient;

    @Autowired
    private PromptBuilder promptBuilder;

    @Autowired
    private ResponseParser responseParser;

    // Custom exception to signal AI was down (not a code quality failure)
    public static class AiUnavailableException extends RuntimeException {
        public AiUnavailableException(String message) {
            super(message);
        }
    }

    public JsonNode analyzeCode(String code) {

        if (code == null || code.isBlank()) {
            System.out.println("⚠️ Empty code received, skipping...");
            return null;
        }

        if (code.length() > 6000) {
            code = code.substring(0, 6000);
        }

        String prompt = promptBuilder.buildSecurityPrompt(code);

        int maxRetries = 4;

        for (int i = 0; i < maxRetries; i++) {
            try {
                System.out.println("🧠 Gemini Analysis Attempt " + (i + 1));

                String response = callGemini(prompt);
                return responseParser.parse(response);

            } catch (HttpClientErrorException e) {

                HttpStatus status = (HttpStatus) e.getStatusCode();
                System.out.println("❌ HTTP Client Error: " + status);

                // Don't retry on 400 - prompt is broken, fix it
                if (status == HttpStatus.BAD_REQUEST) {
                    throw new RuntimeException("Bad request - fix prompt", e);
                }

                // Rate limited - wait longer before retry
                if (status == HttpStatus.TOO_MANY_REQUESTS) {
                    System.out.println("⏳ Rate limited (429). Waiting 20s before retry...");
                    sleepSilently(20000);
                } else {
                    throw e; // 4xx errors other than 429 → fail fast, don't retry
                }

            } catch (HttpServerErrorException e) {

                HttpStatus status = (HttpStatus) e.getStatusCode();

                // 503 = Gemini overloaded → exponential backoff
                if (status == HttpStatus.SERVICE_UNAVAILABLE) {
                    long waitMs = (long) Math.pow(2, i + 1) * 1000; // 2s, 4s, 8s, 16s
                    System.out.println("⏳ Gemini 503 (overloaded). Attempt " + (i + 1)
                            + ". Waiting " + waitMs + "ms before retry...");

                    if (i == maxRetries - 1) {
                        // All retries exhausted — AI is down, not a code problem
                        throw new AiUnavailableException("Gemini service unavailable after "
                                + maxRetries + " attempts");
                    }

                    sleepSilently(waitMs);

                } else {
                    // Other 5xx errors → fail fast
                    throw new AiUnavailableException("Gemini server error: " + status);
                }

            } catch (Exception e) {
                System.out.println("❌ Unexpected error on attempt " + (i + 1) + ": "
                        + e.getMessage());
                e.printStackTrace();
                sleepSilently(1000);
            }
        }

        throw new AiUnavailableException("Gemini failed after " + maxRetries + " retries");
    }

    private String callGemini(String prompt) {
        return aiClient.callGemini(prompt, "gemini-2.5-flash");
    }

    private void sleepSilently(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
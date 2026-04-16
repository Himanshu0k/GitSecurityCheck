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

package security.git.McaProject.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public JsonNode analyzeCode(String code) {

        if (code == null || code.isBlank()) {
            System.out.println("⚠️ Empty code received, skipping...");
            return null;
        }

        // limit payload
        if (code.length() > 6000) {
            code = code.substring(0, 6000);
        }

        String prompt = promptBuilder.buildSecurityPrompt(code);

        int maxRetries = 3;

        for (int i = 0; i < maxRetries; i++) {
            try {
                System.out.println("🧠 Gemini Analysis Attempt " + (i + 1));

                String response = callGeminiWithFallback(prompt);

                // parse + return
                return responseParser.parse(response);

            } catch (Exception e) {
                System.out.println("❌ Attempt failed: " + (i + 1));
                e.printStackTrace();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }

        throw new RuntimeException("Gemini failed after retries");
    }

    private String callGeminiWithFallback(String prompt) {
        try {
            return aiClient.callGemini(prompt, "gemini-2.5-flash");
        } catch (Exception e) {
            System.out.println("⚠️ Falling back to gemini-1.5-flash");
//            return aiClient.callGemini(prompt, "gemini-1.5-flash");
            return aiClient.callGemini(prompt, "gemini-2.5-flash");

        }
    }
}
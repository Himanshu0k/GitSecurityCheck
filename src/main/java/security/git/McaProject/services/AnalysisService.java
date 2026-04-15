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
////    public void analyzeCode(String code) {
////
//////        System.out.println("🧠 Sending code to Gemini...");
////
//////        String prompt = promptBuilder.buildSecurityPrompt(code);
//////
//////        String response = aiClient.callGemini(prompt);
////        System.out.println("🧠 Sending code to Gemini...");
////        aiClient.callGemini(code);
////
//////        responseParser.parse(response);
////    }
//public void analyzeCode(String code) {
//
//    // ✅ Limit payload size
//    if (code.length() > 6000) {
//        code = code.substring(0, 6000);
//    }
//
//    int maxRetries = 3;
//
//    for (int i = 0; i < maxRetries; i++) {
//        try {
//            System.out.println("🧠 Sending code to Gemini... Attempt " + (i + 1));
//
//            // 👉 your existing API call here
//            callGemini(code);
//
//            // ✅ Add delay to avoid rate limit
//            Thread.sleep(1500);
//
//            return; // success → exit
//
//        } catch (Exception e) {
//            System.out.println("❌ Gemini failed. Retrying...");
//            e.printStackTrace();
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ignored) {}
//        }
//    }
//
//    System.out.println("❌ Gemini failed after retries");
//}
//    public void callGemini(String code) {
//        try {
//            // Try latest model first
//            sendRequest("gemini-2.5-flash", code);
//
//        } catch (Exception e) {
//            System.out.println("⚠️ Falling back to gemini-1.5-flash");
//
//            // Fallback
//            sendRequest("gemini-1.5-flash", code);
//        }
//    }
//
//    private void sendRequest(String model, String code) {
//
//        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
//                + model + ":generateContent?key=" + apiKey;
//
//        // your existing RestTemplate logic here
//    }
//}

package security.git.McaProject.services;

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

    public void analyzeCode(String code) {

        // ✅ Limit payload size
        if (code.length() > 6000) {
            code = code.substring(0, 6000);
        }

        String prompt = promptBuilder.buildSecurityPrompt(code);

        int maxRetries = 3;

        for (int i = 0; i < maxRetries; i++) {
            try {
                System.out.println("🧠 Sending code to Gemini... Attempt " + (i + 1));

                String response = callGeminiWithFallback(prompt);

                // ✅ Parse response (important for your project goal)
                responseParser.parse(response);

                // ✅ Small delay to avoid rate limiting
                Thread.sleep(1500);

                return; // success → exit

            } catch (Exception e) {
                System.out.println("❌ Gemini failed. Retrying...");
                e.printStackTrace();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }

        System.out.println("❌ Gemini failed after all retries");
    }

    private String callGeminiWithFallback(String prompt) {

        try {
            // ✅ Try latest model first
            return aiClient.callGemini(prompt, "gemini-2.5-flash");

        } catch (Exception e) {
            System.out.println("⚠️ Falling back to gemini-1.5-flash");

            // ✅ Fallback model
            return aiClient.callGemini(prompt, "gemini-1.5-flash");
        }
    }
}
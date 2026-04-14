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

        System.out.println("🧠 Sending code to Gemini...");

        String prompt = promptBuilder.buildSecurityPrompt(code);

        String response = aiClient.callGemini(prompt);

        responseParser.parse(response);
    }
}
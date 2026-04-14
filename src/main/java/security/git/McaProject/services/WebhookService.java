package security.git.McaProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    @Autowired
    private AnalysisService analysisService;

    public void processWebhook(String payload, String eventType) {

        System.out.println("📩 Event Received: " + eventType);

        if ("ping".equals(eventType)) {
            System.out.println("✅ GitHub webhook setup successful!");
            return;
        }

        if ("push".equals(eventType)) {
            System.out.println("🚀 Push event detected!");

            // 👉 For now, send raw payload (next step: extract files properly)
            analysisService.analyzeCode(payload);
        }
    }
}
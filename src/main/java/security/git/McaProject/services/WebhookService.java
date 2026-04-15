package security.git.McaProject.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    private final GithubApiService githubApiService;
    private final AnalysisService analysisService;

    public WebhookService(GithubApiService githubApiService,
                          AnalysisService analysisService) {
        this.githubApiService = githubApiService;
        this.analysisService = analysisService;
    }

    public void processWebhook(String payload, String eventType) {

        System.out.println("📩 Event Received: " + eventType);

        if ("push".equals(eventType)) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(payload);

                String repoName = root.get("repository").get("name").asText();
                String owner = root.get("repository").get("owner").get("login").asText();

                JsonNode commits = root.get("commits");

                for (JsonNode commit : commits) {

                    JsonNode addedFiles = commit.get("added");
                    JsonNode modifiedFiles = commit.get("modified");

                    // Process added files
                    processFiles(owner, repoName, addedFiles);

                    // Process modified files
                    processFiles(owner, repoName, modifiedFiles);
                }

            } catch (Exception e) {
                System.out.println("❌ Error processing webhook: " + e.getMessage());
            }
        }
    }

    private void processFiles(String owner, String repo, JsonNode files) {
        if (files == null) return;

        for (JsonNode fileNode : files) {
            String filePath = fileNode.asText();

            // Only analyze code files
            if (!filePath.endsWith(".java") && !filePath.endsWith(".js")) {
                continue;
            }

            System.out.println("📂 Fetching file: " + filePath);

            String code = githubApiService.getFileContent(owner, repo, filePath);

            if (code != null) {
                System.out.println("🧠 Sending REAL CODE to Gemini...");
                analysisService.analyzeCode(code);
            }
        }
    }
}
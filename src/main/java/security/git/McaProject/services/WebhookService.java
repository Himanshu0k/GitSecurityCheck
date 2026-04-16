//package security.git.McaProject.services;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class WebhookService {
//
//    private final GithubApiService githubApiService;
//    private final AnalysisService analysisService;
//
//    public WebhookService(GithubApiService githubApiService,
//                          AnalysisService analysisService) {
//        this.githubApiService = githubApiService;
//        this.analysisService = analysisService;
//    }
//
////    public void processWebhook(String payload, String eventType) {
////
////        System.out.println("📩 Event Received: " + eventType);
////
////        if ("push".equals(eventType)) {
////            try {
////                ObjectMapper mapper = new ObjectMapper();
////                JsonNode root = mapper.readTree(payload);
////
////                String repoName = root.get("repository").get("name").asText();
////                System.out.println("Repo Name: " + repoName);
////                String owner = root.get("repository").get("owner").get("login").asText();
////                System.out.println("Owner: " + owner);
////
////                JsonNode commits = root.get("commits");
////
////                for (JsonNode commit : commits) {
////
////                    JsonNode addedFiles = commit.get("added");
////                    JsonNode modifiedFiles = commit.get("modified");
////
////                    // Process added files
////                    processFiles(owner, repoName, addedFiles);
////
////                    // Process modified files
////                    processFiles(owner, repoName, modifiedFiles);
////                }
////
////            } catch (Exception e) {
////                System.out.println("❌ Error processing webhook: " + e.getMessage());
////            }
////        }
////    }
//public void processWebhook(Map<String, Object> payload) {
//
//    System.out.println("🚀 Processing webhook...");
//
//    Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
//    Map<String, Object> ownerMap = (Map<String, Object>) repository.get("owner");
//
//    String owner = (String) ownerMap.get("login");
//    String repo = (String) repository.get("name");
//
//    String ref = (String) payload.get("ref");
//    String branch = ref.replace("refs/heads/", "");
//
//    System.out.println("Owner: " + owner);
//    System.out.println("Repo: " + repo);
//    System.out.println("Branch: " + branch);
//
//    List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
//
//    if (commits == null) {
//        System.out.println("⚠️ No commits found");
//        return;
//    }
//
//    for (Map<String, Object> commit : commits) {
//
//        List<String> added = (List<String>) commit.get("added");
//        List<String> modified = (List<String>) commit.get("modified");
//
//        processFiles(owner, repo, branch, added);
//        processFiles(owner, repo, branch, modified);
//    }
//}
//
////    private void processFiles(String owner, String repo, JsonNode files) {
////        if (files == null) return;
////
////        for (JsonNode fileNode : files) {
////            String filePath = fileNode.asText();
////
////            // Only analyze code files
////            if (!filePath.endsWith(".java") && !filePath.endsWith(".js")) {
////                continue;
////            }
////
////            System.out.println("📂 Fetching file: " + filePath);
////
////            String code = githubApiService.getFileContent(owner, repo, filePath);
////
////            if (code != null) {
////                System.out.println("🧠 Sending REAL CODE to Gemini...");
////                analysisService.analyzeCode(code);
////            }
////        }
////    }
//private void processFiles(String owner, String repo, String branch, List<String> files) {
//    if (files == null) return;
//
//    for (String filePath : files) {
//        System.out.println("📂 Fetching file: " + filePath);
//
//        // ✅ Add filter here
//        if (!filePath.endsWith(".java") && !filePath.endsWith(".js")) {
//            continue;
//        }
//
//        System.out.println("📂 Fetching file: " + filePath);
//
//        try {
//            String code = githubApiService.getFileContent(owner, repo, branch, filePath);
//            analysisService.analyzeCode(code);
//
//        } catch (Exception e) {
//            System.out.println("❌ Error fetching file: " + filePath);
//            e.printStackTrace();
//        }
//    }
//}
//}

package security.git.McaProject.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WebhookService {

    private final GithubApiService githubApiService;
    private final AnalysisService analysisService;

    public WebhookService(GithubApiService githubApiService,
                          AnalysisService analysisService) {
        this.githubApiService = githubApiService;
        this.analysisService = analysisService;
    }

    public void processWebhook(Map<String, Object> payload) {

        System.out.println("🚀 Processing webhook...");

        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
        if (repository == null) return;

        Map<String, Object> ownerMap = (Map<String, Object>) repository.get("owner");

        String owner = (String) ownerMap.get("login");
        String repo = (String) repository.get("name");

        String ref = (String) payload.get("ref");
        String branch = ref.replace("refs/heads/", "");

        System.out.println("Owner: " + owner);
        System.out.println("Repo: " + repo);
        System.out.println("Branch: " + branch);

        List<Map<String, Object>> commits =
                (List<Map<String, Object>>) payload.get("commits");

        if (commits == null || commits.isEmpty()) {
            System.out.println("⚠️ No commits found");
            return;
        }

        for (Map<String, Object> commit : commits) {

            String commitSha = (String) commit.get("id");

            processFiles(owner, repo, branch, commitSha,
                    (List<String>) commit.get("added"));

            processFiles(owner, repo, branch, commitSha,
                    (List<String>) commit.get("modified"));
        }
    }

    private void processFiles(String owner,
                              String repo,
                              String branch,
                              String commitSha,
                              List<String> files) {

        if (files == null || files.isEmpty()) return;

        for (String filePath : files) {

            if (!filePath.endsWith(".java") && !filePath.endsWith(".js")) {
                continue;
            }

            System.out.println("📂 Processing file: " + filePath);

            try {
                String code = githubApiService.getFileContent(owner, repo, branch, filePath);

                JsonNode result = analysisService.analyzeCode(code);

                if (result != null) {
                    githubApiService.postComment(owner, repo, commitSha, result);
                }

            } catch (Exception e) {
                System.out.println("❌ Failed: " + filePath);
                e.printStackTrace();
            }
        }
    }
}
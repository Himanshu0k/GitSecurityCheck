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

//package security.git.McaProject.services;
//
//import com.fasterxml.jackson.databind.JsonNode;
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
//    public void processWebhook(Map<String, Object> payload) {
//
//        System.out.println("🚀 Processing webhook...");
//
//        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
//        if (repository == null) return;
//
//        Map<String, Object> ownerMap = (Map<String, Object>) repository.get("owner");
//
//        String owner = (String) ownerMap.get("login");
//        String repo = (String) repository.get("name");
//
//        String ref = (String) payload.get("ref");
//        String branch = ref.replace("refs/heads/", "");
//
//        System.out.println("Owner: " + owner);
//        System.out.println("Repo: " + repo);
//        System.out.println("Branch: " + branch);
//
//        List<Map<String, Object>> commits =
//                (List<Map<String, Object>>) payload.get("commits");
//
//        if (commits == null || commits.isEmpty()) {
//            System.out.println("⚠️ No commits found");
//            return;
//        }
//
//        for (Map<String, Object> commit : commits) {
//
//            String commitSha = (String) commit.get("id");
//
//            processFiles(owner, repo, branch, commitSha,
//                    (List<String>) commit.get("added"));
//
//            processFiles(owner, repo, branch, commitSha,
//                    (List<String>) commit.get("modified"));
//        }
//    }
//
//    private void processFiles(String owner,
//                              String repo,
//                              String branch,
//                              String commitSha,
//                              List<String> files) {
//
//        if (files == null || files.isEmpty()) return;
//
//        for (String filePath : files) {
//
//            if (!filePath.endsWith(".java") && !filePath.endsWith(".js")) {
//                continue;
//            }
//
//            System.out.println("📂 Processing file: " + filePath);
//
//            try {
//                String code = githubApiService.getFileContent(owner, repo, branch, filePath);
//
//                JsonNode result = analysisService.analyzeCode(code);
//
//                if (result != null) {
//                    githubApiService.postComment(owner, repo, commitSha, result);
//                }
//
//            } catch (Exception e) {
//                System.out.println("❌ Failed: " + filePath);
//                e.printStackTrace();
//            }
//        }
//    }
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

//    public void processWebhook(Map<String, Object> payload) {
//
//        System.out.println("🚀 Processing webhook...");
//
//        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
//        if (repository == null) return;
//
//        Map<String, Object> ownerMap = (Map<String, Object>) repository.get("owner");
//
//        String owner = (String) ownerMap.get("login");
//        String repo = (String) repository.get("name");
//
//        String ref = (String) payload.get("ref");
//        String branch = ref.replace("refs/heads/", "");
//
//        System.out.println("Owner: " + owner);
//        System.out.println("Repo: " + repo);
//        System.out.println("Branch: " + branch);
//
//        List<Map<String, Object>> commits =
//                (List<Map<String, Object>>) payload.get("commits");
//
//        if (commits == null || commits.isEmpty()) {
//            System.out.println("⚠️ No commits found");
//            return;
//        }
//
//        for (Map<String, Object> commit : commits) {
//
//            String commitSha = (String) commit.get("id");
//
//            // 🔥 Track overall status for this commit
//            boolean hasFailure = false;
//
//            hasFailure |= processFiles(owner, repo, branch, commitSha,
//                    (List<String>) commit.get("added"));
//
//            hasFailure |= processFiles(owner, repo, branch, commitSha,
//                    (List<String>) commit.get("modified"));
//
//            // ✅ Final commit status
//            String finalStatus = hasFailure ? "FAIL" : "PASS";
//
//            System.out.println("📊 Final Commit Status: " + finalStatus);
//
//            githubApiService.setCommitStatus(owner, repo, commitSha, finalStatus);
//        }
//    }

    public void processWebhook(Map<String, Object> payload) {

        System.out.println("🚀 Processing webhook...");

        // ── Extract repository info ──────────────────────────────────────────
        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
        if (repository == null) {
            System.out.println("⚠️ No repository info in payload, aborting.");
            return;
        }

        Map<String, Object> ownerMap = (Map<String, Object>) repository.get("owner");
        if (ownerMap == null) {
            System.out.println("⚠️ No owner info in payload, aborting.");
            return;
        }

        String owner  = (String) ownerMap.get("login");
        String repo   = (String) repository.get("name");
        String ref    = (String) payload.get("ref");

        if (owner == null || repo == null || ref == null) {
            System.out.println("⚠️ Missing owner/repo/ref, aborting.");
            return;
        }

        String branch = ref.replace("refs/heads/", "");

        System.out.println("Owner:  " + owner);
        System.out.println("Repo:   " + repo);
        System.out.println("Branch: " + branch);

        // ── Extract commits ──────────────────────────────────────────────────
        List<Map<String, Object>> commits =
                (List<Map<String, Object>>) payload.get("commits");

        if (commits == null || commits.isEmpty()) {
            System.out.println("⚠️ No commits found in payload.");
            return;
        }

        // ── Process each commit ──────────────────────────────────────────────
        for (Map<String, Object> commit : commits) {

            String commitSha = (String) commit.get("id");

            if (commitSha == null) {
                System.out.println("⚠️ Commit SHA missing, skipping.");
                continue;
            }

            System.out.println("\n─────────────────────────────────────────");
            System.out.println("🔍 Analyzing commit: " + commitSha);

            boolean hasFailure  = false;
            boolean aiWasDown   = false;

            // Process added files
            try {
                hasFailure |= processFiles(owner, repo, branch, commitSha,
                        (List<String>) commit.get("added"));

            } catch (AnalysisService.AiUnavailableException e) {
                System.out.println("⚠️ AI down while processing added files: " + e.getMessage());
                aiWasDown = true;
            }

            // Process modified files
            try {
                hasFailure |= processFiles(owner, repo, branch, commitSha,
                        (List<String>) commit.get("modified"));

            } catch (AnalysisService.AiUnavailableException e) {
                System.out.println("⚠️ AI down while processing modified files: " + e.getMessage());
                aiWasDown = true;
            }

            // ── Set final GitHub commit status ───────────────────────────────
            try {
                if (hasFailure) {
                    // Real code issues found
                    System.out.println("📊 Final Commit Status: FAIL");
                    githubApiService.setCommitStatus(owner, repo, commitSha,
                            "failure", "Issues detected in code");

                } else if (aiWasDown) {
                    // AI was unavailable — don't blame the code
                    System.out.println("📊 Final Commit Status: ERROR (AI unavailable)");
                    githubApiService.setCommitStatus(owner, repo, commitSha,
                            "error", "AI review unavailable — re-run or review manually");

                } else {
                    // All files passed
                    System.out.println("📊 Final Commit Status: PASS");
                    githubApiService.setCommitStatus(owner, repo, commitSha,
                            "success", "All checks passed");
                }

            } catch (Exception e) {
                System.out.println("❌ Failed to set commit status for: " + commitSha);
                e.printStackTrace();
            }

            System.out.println("─────────────────────────────────────────\n");
        }
    }

//    private boolean processFiles(String owner,
//                                 String repo,
//                                 String branch,
//                                 String commitSha,
//                                 List<String> files) {
//
//        if (files == null || files.isEmpty()) return false;
//
//        boolean hasFailure = false;
//
//        for (String filePath : files) {
//
//            if (!filePath.endsWith(".java") && !filePath.endsWith(".js")) {
//                continue;
//            }
//
//            System.out.println("📂 Processing file: " + filePath);
//
//            try {
//                String code = githubApiService.getFileContent(owner, repo, branch, filePath);
//
//                JsonNode result = analysisService.analyzeCode(code);
//
//                if (result != null) {
//
//                    // ✅ Post comment
//                    githubApiService.postComment(owner, repo, commitSha, result);
//
//                    // ✅ Check status
//                    if (result.has("status")) {
//                        String status = result.get("status").asText();
//
//                        if ("FAIL".equalsIgnoreCase(status)) {
//                            hasFailure = true;
//                        }
//                    } else {
//                        // safer fallback
//                        hasFailure = true;
//                    }
//                }
//
//            } catch (Exception e) {
//                System.out.println("❌ Failed: " + filePath);
//                e.printStackTrace();
//
//                // treat errors as failure
//                hasFailure = true;
//            }
//        }
//
//        return hasFailure;
//    }
private boolean processFiles(String owner,
                             String repo,
                             String branch,
                             String commitSha,
                             List<String> files) {

    if (files == null || files.isEmpty()) return false;

    boolean hasFailure = false;
    boolean aiWasDown = false;

    for (String filePath : files) {

        // Only process Java and JS files
        if (!filePath.endsWith(".java") && !filePath.endsWith(".js")) {
            System.out.println("⏭️ Skipping unsupported file: " + filePath);
            continue;
        }

        System.out.println("📂 Processing file: " + filePath);

        try {
            String code = githubApiService.getFileContent(owner, repo, branch, filePath);

            JsonNode result = analysisService.analyzeCode(code);

            if (result == null) {
                // Empty file — skip silently
                System.out.println("⚠️ Empty or null result for: " + filePath + ", skipping.");
                continue;
            }

            // ✅ Post comment to GitHub
            githubApiService.postComment(owner, repo, commitSha, result);

            // ✅ Check analysis status
            if (result.has("status")) {
                String status = result.get("status").asText();
                System.out.println("📊 File status [" + filePath + "]: " + status);

                if ("FAIL".equalsIgnoreCase(status)) {
                    hasFailure = true;
                    System.out.println("❌ Failed: " + filePath);
                } else {
                    System.out.println("✅ Passed: " + filePath);
                }

            } else {
                // Response came back but had no status field — treat as failure
                System.out.println("⚠️ No status field in result for: " + filePath);
                hasFailure = true;
            }

        } catch (AnalysisService.AiUnavailableException e) {
            // Gemini was down — NOT a code quality failure
            // Don't penalize the commit for an infrastructure issue
            System.out.println("⚠️ AI unavailable for file: " + filePath + " — " + e.getMessage());
            aiWasDown = true;
            // Continue processing remaining files instead of stopping

        } catch (Exception e) {
            // Unexpected error (network, GitHub API, parsing, etc.)
            System.out.println("❌ Unexpected error processing file: " + filePath);
            e.printStackTrace();
            hasFailure = true;
        }
    }

    // If AI was down but no actual code failures were found, throw so
    // the caller can set GitHub status to "error" instead of "failure"
    if (aiWasDown && !hasFailure) {
        throw new AnalysisService.AiUnavailableException(
                "AI review service was unavailable for one or more files"
        );
    }

    // If both AI was down AND there were real failures, still return true
    // so the commit is correctly marked as failure
    return hasFailure;
}
}
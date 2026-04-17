//package security.git.McaProject.services;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Base64;
//import java.util.Map;
//
//@Service
//public class GithubApiService {
//
//    @Value("${github.token}")
//    private String githubToken;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
////    public String getFileContent(String owner, String repo, String path) {
////        try {
////            String url = "https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path;
////
////            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
////            headers.set("Authorization", "Bearer " + githubToken);
////            headers.set("Accept", "application/vnd.github.v3+json");
////
////            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
////
////            org.springframework.http.ResponseEntity<Map> response =
////                    restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
////
////            String encodedContent = (String) response.getBody().get("content");
////
////            // GitHub returns base64 → decode it
////            byte[] decodedBytes = Base64.getDecoder().decode(encodedContent.replaceAll("\\n", ""));
////            return new String(decodedBytes);
////
////        } catch (Exception e) {
////            System.out.println("❌ Error fetching file: " + path);
////            return null;
////        }
////    }
//public String getFileContent(String owner, String repo, String branch, String filePath) {
//
//    String url = "https://api.github.com/repos/" + owner + "/" + repo +
//            "/contents/" + filePath + "?ref=" + branch;
//
//    System.out.println("Final URL: " + url);
//
//    RestTemplate restTemplate = new RestTemplate();
//
//    try {
//        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//
//        if (response == null || !response.containsKey("content")) {
//            throw new RuntimeException("Invalid response from GitHub");
//        }
//
//        String encodedContent = (String) response.get("content");
//        encodedContent = encodedContent.replace("\n", "");
//
//        // ✅ Decode Base64
//        byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedContent);
//        String decodedContent = new String(decodedBytes);
//
//        return decodedContent;
//
//    } catch (Exception e) {
//        throw new RuntimeException("Failed to fetch file: " + filePath, e);
//    }
//}
//}

package security.git.McaProject.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class GithubApiService {

    @Value("${github.token}")
    private String githubToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getFileContent(String owner, String repo, String branch, String filePath) {

        String url = "https://api.github.com/repos/" + owner + "/" + repo +
                "/contents/" + filePath + "?ref=" + branch;

        System.out.println("🌐 GitHub URL: " + url);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            headers.set("Accept", "application/vnd.github.v3+json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Object> body = response.getBody();

            if (body == null || !body.containsKey("content")) {
                throw new RuntimeException("Invalid GitHub response");
            }

            String encodedContent = (String) body.get("content");
            encodedContent = encodedContent.replace("\n", "");

            byte[] decodedBytes = Base64.getDecoder().decode(encodedContent);
            return new String(decodedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch file: " + filePath, e);
        }
    }

    public void postComment(String owner, String repo, String commitSha, JsonNode analysis) {

        if (analysis == null) {
            System.out.println("⚠️ Skipping comment: analysis is null");
            return;
        }

        String url = "https://api.github.com/repos/" + owner + "/" + repo
                + "/commits/" + commitSha + "/comments";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("Accept", "application/vnd.github+json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        StringBuilder comment = new StringBuilder();
        comment.append("## 🤖 AI Code Review\n\n");

        // ✅ Safe status extraction
        String status = getSafe(analysis, "status");
        comment.append("**Status:** ").append(status).append("\n\n");

        JsonNode issuesNode = analysis.get("issues");

        if (issuesNode != null && issuesNode.isArray() && issuesNode.size() > 0) {

            for (JsonNode issue : issuesNode) {

                String severity = getSafe(issue, "severity");
                String type = getSafe(issue, "type");
                String description = getSafe(issue, "description");
                String line = getSafe(issue, "line");

                comment.append("- **").append(severity).append("** | ")
                        .append(type).append("\n")
                        .append("  → ").append(description);

                if (!line.equals("N/A")) {
                    comment.append(" (line ").append(line).append(")");
                }

                comment.append("\n\n");
            }

        } else {
            comment.append("✅ No issues found.\n");
        }

        Map<String, String> body = Map.of("body", comment.toString());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(body);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            System.out.println("✅ Comment posted to GitHub");

        } catch (Exception e) {
            System.out.println("❌ Failed to post comment");
            e.printStackTrace();
        }
    }

    public void setCommitStatus(String owner, String repo, String commitSha, String status) {

        String url = "https://api.github.com/repos/" + owner + "/" + repo
                + "/statuses/" + commitSha;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("Accept", "application/vnd.github+json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // map AI status → GitHub status
        String state = "success";
        if ("FAIL".equalsIgnoreCase(status)) {
            state = "failure";
        }

        Map<String, String> body = Map.of(
                "state", state,
                "context", "AI Code Review",
                "description", state.equals("success")
                        ? "No issues found"
                        : "Issues detected in code"
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(body);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            System.out.println("✅ Commit status set: " + state);

        } catch (Exception e) {
            System.out.println("❌ Failed to set commit status");
            e.printStackTrace();
        }
    }

    private String getSafe(JsonNode node, String field) {
        if (node == null) return "N/A";

        JsonNode value = node.get(field);

        if (value == null || value.isNull()) {
            return "N/A";
        }

        return value.asText();
    }
}
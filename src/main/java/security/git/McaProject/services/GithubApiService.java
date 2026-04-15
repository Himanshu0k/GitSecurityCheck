package security.git.McaProject.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class GithubApiService {

    @Value("${github.token}")
    private String githubToken;

    private final RestTemplate restTemplate = new RestTemplate();

//    public String getFileContent(String owner, String repo, String path) {
//        try {
//            String url = "https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path;
//
//            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
//            headers.set("Authorization", "Bearer " + githubToken);
//            headers.set("Accept", "application/vnd.github.v3+json");
//
//            org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
//
//            org.springframework.http.ResponseEntity<Map> response =
//                    restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class);
//
//            String encodedContent = (String) response.getBody().get("content");
//
//            // GitHub returns base64 → decode it
//            byte[] decodedBytes = Base64.getDecoder().decode(encodedContent.replaceAll("\\n", ""));
//            return new String(decodedBytes);
//
//        } catch (Exception e) {
//            System.out.println("❌ Error fetching file: " + path);
//            return null;
//        }
//    }
public String getFileContent(String owner, String repo, String branch, String path) {

    String url = "https://api.github.com/repos/" + owner + "/" + repo +
            "/contents/" + path + "?ref=" + branch;

    System.out.println("Final URL: " + url);

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

    String content = (String) response.getBody().get("content");

    return new String(Base64.getDecoder().decode(content));
}
}
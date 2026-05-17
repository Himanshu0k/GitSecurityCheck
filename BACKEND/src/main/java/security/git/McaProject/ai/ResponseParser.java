////package security.git.McaProject.ai;
////
////import org.springframework.stereotype.Component;
////
////@Component
////public class ResponseParser {
////
////    public void parse(String response) {
////
////        System.out.println("📊 Gemini Response:");
////        System.out.println(response);
////
////        // Next step:
////        // - extract JSON
////        // - map to Java object
////        // - store in DB
////    }
////}
//
//package security.git.McaProject.ai;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ResponseParser {
//
////    public JsonNode parse(String rawResponse) {
////        try {
////            String cleaned = rawResponse
////                    .replace("```json", "")
////                    .replace("```", "")
////                    .trim();
////
////            ObjectMapper mapper = new ObjectMapper();
////            return mapper.readTree(cleaned);
////
////        } catch (Exception e) {
////            throw new RuntimeException("Failed to parse Gemini response", e);
////        }
////    }
//
//    public JsonNode parse(String response) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(response);
//
//            // 👇 Extract Gemini text output
//            String text = root
//                    .get("candidates")
//                    .get(0)
//                    .get("content")
//                    .get("parts")
//                    .get(0)
//                    .get("text")
//                    .asText();
//
//            System.out.println("🧠 RAW AI TEXT:\n" + text);
//
//            // 👇 Clean markdown if present
//            text = text.replace("```json", "")
//                    .replace("```", "")
//                    .trim();
//
//            // 👇 Convert string JSON → actual JSON
//            return mapper.readTree(text);
//
//        } catch (Exception e) {
//            System.out.println("❌ Parsing failed");
//            e.printStackTrace();
//            return null;
//        }
//    }
//}

package security.git.McaProject.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ResponseParser {

    public ParsedAuditData parse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            // Extract Gemini text output
            String text = root
                    .get("candidates")
                    .get(0)
                    .get("content")
                    .get("parts")
                    .get(0)
                    .get("text")
                    .asText();

            System.out.println("🧠 RAW AI TEXT:\n" + text);

            // Clean markdown if present
            text = text.replace("```json", "")
                    .replace("```", "")
                    .trim();

            // Convert string JSON → actual JSON
            JsonNode auditData = mapper.readTree(text);

            return extractAuditData(auditData, text);

        } catch (Exception e) {
            System.out.println("❌ Parsing failed");
            e.printStackTrace();
            return null;
        }
    }

    private ParsedAuditData extractAuditData(JsonNode auditData, String rawText) {
        ParsedAuditData data = new ParsedAuditData();

        // Extract vulnerability counts
        if (auditData.has("vulnerabilities")) {
            JsonNode vulns = auditData.get("vulnerabilities");
            data.setCriticalCount(vulns.has("critical") ? vulns.get("critical").asInt() : 0);
            data.setHighCount(vulns.has("high") ? vulns.get("high").asInt() : 0);
            data.setMediumCount(vulns.has("medium") ? vulns.get("medium").asInt() : 0);
            data.setLowCount(vulns.has("low") ? vulns.get("low").asInt() : 0);
        }

        // Store the full JSON for detailed view
        data.setDetailedVulnerabilities(rawText);

        // Determine status
//        int totalVulns = data.getCriticalCount() + data.getHighCount() +
//                data.getMediumCount() + data.getLowCount();
//        data.setStatus(totalVulns == 0 ? "completed" : "completed"); // or "failed" based on your logic

        data.setStatus("completed");
        return data;
    }

    // Inner class to hold parsed data
    public static class ParsedAuditData {
        private int criticalCount;
        private int highCount;
        private int mediumCount;
        private int lowCount;
        private String detailedVulnerabilities;
        private String status;

        // Getters and Setters
        public int getCriticalCount() { return criticalCount; }
        public void setCriticalCount(int criticalCount) { this.criticalCount = criticalCount; }

        public int getHighCount() { return highCount; }
        public void setHighCount(int highCount) { this.highCount = highCount; }

        public int getMediumCount() { return mediumCount; }
        public void setMediumCount(int mediumCount) { this.mediumCount = mediumCount; }

        public int getLowCount() { return lowCount; }
        public void setLowCount(int lowCount) { this.lowCount = lowCount; }

        public String getDetailedVulnerabilities() { return detailedVulnerabilities; }
        public void setDetailedVulnerabilities(String detailedVulnerabilities) {
            this.detailedVulnerabilities = detailedVulnerabilities;
        }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
package security.git.McaProject.ai;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildSecurityPrompt(String code) {

        return """
        You are a senior backend security engineer.

        Analyze the given code and return STRICT JSON output.

        Check for:
        1. Security vulnerabilities (API keys, SQL injection, etc.)
        2. Logical errors
        3. Bad coding practices

        Format:
        {
          "status": "PASS" or "FAIL",
          "issues": [
            {
              "type": "",
              "severity": "HIGH/MEDIUM/LOW",
              "description": "",
              "line": ""
            }
          ]
        }

        CODE:
        """ + code;
    }
}
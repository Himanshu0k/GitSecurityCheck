package security.git.McaProject.middlewares;

//package security.git.McaProject.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "audit_results")
public class AuditResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repositoryName;
    private String commitHash;
    private String commitMessage;
    private String status; // "completed" or "failed"

    @Column(columnDefinition = "TEXT")
    private String rawResponse; // Store the full Gemini response

    private Integer criticalCount = 0;
    private Integer highCount = 0;
    private Integer mediumCount = 0;
    private Integer lowCount = 0;

    @Column(columnDefinition = "TEXT")
    private String vulnerabilitiesJson; // Store detailed vulnerabilities

    private LocalDateTime timestamp;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRepositoryName() { return repositoryName; }
    public void setRepositoryName(String repositoryName) { this.repositoryName = repositoryName; }

    public String getCommitHash() { return commitHash; }
    public void setCommitHash(String commitHash) { this.commitHash = commitHash; }

    public String getCommitMessage() { return commitMessage; }
    public void setCommitMessage(String commitMessage) { this.commitMessage = commitMessage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRawResponse() { return rawResponse; }
    public void setRawResponse(String rawResponse) { this.rawResponse = rawResponse; }

    public Integer getCriticalCount() { return criticalCount; }
    public void setCriticalCount(Integer criticalCount) { this.criticalCount = criticalCount; }

    public Integer getHighCount() { return highCount; }
    public void setHighCount(Integer highCount) { this.highCount = highCount; }

    public Integer getMediumCount() { return mediumCount; }
    public void setMediumCount(Integer mediumCount) { this.mediumCount = mediumCount; }

    public Integer getLowCount() { return lowCount; }
    public void setLowCount(Integer lowCount) { this.lowCount = lowCount; }

    public String getVulnerabilitiesJson() { return vulnerabilitiesJson; }
    public void setVulnerabilitiesJson(String vulnerabilitiesJson) { this.vulnerabilitiesJson = vulnerabilitiesJson; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
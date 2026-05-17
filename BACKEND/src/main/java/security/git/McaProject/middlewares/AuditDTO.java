package security.git.McaProject.middlewares;

//package security.git.McaProject.dto;

import java.time.LocalDateTime;

public class AuditDTO {
    private Long id;
    private String repositoryName;
    private String commitHash;
    private String commitMessage;
    private String status;
    private VulnerabilitiesDTO vulnerabilities;
    private LocalDateTime timestamp;

    public static class VulnerabilitiesDTO {
        private int critical;
        private int high;
        private int medium;
        private int low;

        public VulnerabilitiesDTO(int critical, int high, int medium, int low) {
            this.critical = critical;
            this.high = high;
            this.medium = medium;
            this.low = low;
        }

        // Getters and Setters
        public int getCritical() { return critical; }
        public void setCritical(int critical) { this.critical = critical; }

        public int getHigh() { return high; }
        public void setHigh(int high) { this.high = high; }

        public int getMedium() { return medium; }
        public void setMedium(int medium) { this.medium = medium; }

        public int getLow() { return low; }
        public void setLow(int low) { this.low = low; }
    }

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

    public VulnerabilitiesDTO getVulnerabilities() { return vulnerabilities; }
    public void setVulnerabilities(VulnerabilitiesDTO vulnerabilities) { this.vulnerabilities = vulnerabilities; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
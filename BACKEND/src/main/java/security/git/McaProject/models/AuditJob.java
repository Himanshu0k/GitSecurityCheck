package security.git.McaProject.models;

import jakarta.persistence.*;
import security.git.McaProject.models.enums.JobStatus;

@Entity
public class AuditJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commitId;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
}
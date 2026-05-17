package security.git.McaProject.controllers;

//package security.git.McaProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.git.McaProject.middlewares.AuditDTO;
import security.git.McaProject.middlewares.AuditResult;
import security.git.McaProject.repositories.AuditRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/audits")
@CrossOrigin(origins = "*") // Adjust based on your frontend URL
public class AuditController {

    @Autowired
    private AuditRepository auditRepository;

    @GetMapping
    public ResponseEntity<List<AuditDTO>> getAllAudits() {
        List<AuditResult> audits = auditRepository.findAllByOrderByTimestampDesc();
        List<AuditDTO> dtos = audits.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditDetailDTO> getAuditById(@PathVariable Long id) {
        return auditRepository.findById(id)
                .map(audit -> ResponseEntity.ok(convertToDetailDTO(audit)))
                .orElse(ResponseEntity.notFound().build());
    }

    private AuditDTO convertToDTO(AuditResult audit) {
        AuditDTO dto = new AuditDTO();
        dto.setId(audit.getId());
        dto.setRepositoryName(audit.getRepositoryName());
        dto.setCommitHash(audit.getCommitHash());
        dto.setCommitMessage(audit.getCommitMessage());
        dto.setStatus(audit.getStatus());
        dto.setVulnerabilities(new AuditDTO.VulnerabilitiesDTO(
                audit.getCriticalCount(),
                audit.getHighCount(),
                audit.getMediumCount(),
                audit.getLowCount()
        ));
        dto.setTimestamp(audit.getTimestamp());
        return dto;
    }

    private AuditDetailDTO convertToDetailDTO(AuditResult audit) {
        AuditDetailDTO dto = new AuditDetailDTO();
        dto.setId(audit.getId());
        dto.setRepositoryName(audit.getRepositoryName());
        dto.setCommitHash(audit.getCommitHash());
        dto.setCommitMessage(audit.getCommitMessage());
        dto.setStatus(audit.getStatus());
        dto.setVulnerabilities(new AuditDTO.VulnerabilitiesDTO(
                audit.getCriticalCount(),
                audit.getHighCount(),
                audit.getMediumCount(),
                audit.getLowCount()
        ));
        dto.setTimestamp(audit.getTimestamp());
        dto.setDetailedVulnerabilities(audit.getVulnerabilitiesJson());
        return dto;
    }
}

class AuditDetailDTO extends AuditDTO {
    private String detailedVulnerabilities;

    public String getDetailedVulnerabilities() { return detailedVulnerabilities; }
    public void setDetailedVulnerabilities(String detailedVulnerabilities) {
        this.detailedVulnerabilities = detailedVulnerabilities;
    }
}
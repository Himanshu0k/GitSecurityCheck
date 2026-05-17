package security.git.McaProject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.git.McaProject.middlewares.AuditResult;

import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<AuditResult, Long> {
    List<AuditResult> findAllByOrderByTimestampDesc();
    List<AuditResult> findByRepositoryNameOrderByTimestampDesc(String repositoryName);
}
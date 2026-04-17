package security.git.McaProject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import security.git.McaProject.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
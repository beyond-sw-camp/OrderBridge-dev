package error.pirate.backend.user.command.domain.repository;


import error.pirate.backend.user.command.domain.aggregate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmployeeNo(String userEmployeeNo);
}

package error.pirate.backend.user.command.domain.repository;


import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.aggregate.entity.UserSocialType;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmployeeNo(String userEmployeeNo);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByUserSocialTypeAndUserSocialId(UserSocialType userSocialType, String userSocialId);
}

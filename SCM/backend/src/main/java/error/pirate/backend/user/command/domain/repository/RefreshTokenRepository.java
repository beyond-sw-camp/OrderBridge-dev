package error.pirate.backend.user.command.domain.repository;

import error.pirate.backend.user.command.domain.aggregate.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface  RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUserEmployeeNoAndRefreshToken(String userEmployeeNo, String refreshToken);
}

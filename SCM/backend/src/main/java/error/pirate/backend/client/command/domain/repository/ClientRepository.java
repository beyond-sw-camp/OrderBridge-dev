package error.pirate.backend.client.command.domain.repository;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}

package error.pirate.backend.client.command.domain.repository;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.client.command.domain.aggregate.entity.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // 활성화된 거래처만 조회
    List<Client> findAllByClientStatus(ClientStatus clientStatus);
}

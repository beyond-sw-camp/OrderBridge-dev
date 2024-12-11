package error.pirate.backend.productionReceiving.command.domain.repository;

import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.query.repository.ProductionReceivingQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionReceivingRepository extends JpaRepository<ProductionReceiving, Long>, ProductionReceivingQueryRepository {
}

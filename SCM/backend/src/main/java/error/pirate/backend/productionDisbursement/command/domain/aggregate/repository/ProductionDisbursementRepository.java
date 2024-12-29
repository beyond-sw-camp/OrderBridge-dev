package error.pirate.backend.productionDisbursement.command.domain.aggregate.repository;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionDisbursementRepository extends JpaRepository<ProductionDisbursement, Long> {
}

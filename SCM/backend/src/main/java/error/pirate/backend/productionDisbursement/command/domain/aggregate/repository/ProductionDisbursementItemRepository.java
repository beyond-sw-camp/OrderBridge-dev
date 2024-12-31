package error.pirate.backend.productionDisbursement.command.domain.aggregate.repository;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursement;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductionDisbursementItemRepository extends JpaRepository<ProductionDisbursementItem, Long> {

    List<ProductionDisbursementItem> findAllByProductionDisbursement(ProductionDisbursement productionDisbursement);
}

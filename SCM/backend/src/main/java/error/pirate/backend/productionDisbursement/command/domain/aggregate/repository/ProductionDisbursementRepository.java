package error.pirate.backend.productionDisbursement.command.domain.aggregate.repository;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductionDisbursementRepository extends JpaRepository<ProductionDisbursement, Long> {

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ProductionDisbursement pd " +
            "WHERE pd.workOrder.workOrderSeq = :workOrderSeq " +
            "AND pd.productionDisbursementStatus != 'DELETE'")
    boolean existsByWorkOrderSeqAndProductionDisbursementStatus(Long workOrderSeq);
}

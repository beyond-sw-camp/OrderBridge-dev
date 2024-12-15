package error.pirate.backend.workOrder.command.domain.repository;

import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

@Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
        "FROM WorkOrder w " +
        "WHERE w.salesOrder.salesOrderSeq = :salesOrderSeq " +
        "AND w.item.itemSeq = :itemSeq")
    boolean existsBySalesOrderSeqAndItemSeq(@Param("salesOrderSeq")Long salesOrderSeq,
                                            @Param("itemSeq")Long itemSeq);
}

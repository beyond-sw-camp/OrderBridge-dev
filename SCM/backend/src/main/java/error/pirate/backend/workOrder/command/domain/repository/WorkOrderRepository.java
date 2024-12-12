package error.pirate.backend.workOrder.command.domain.repository;


import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    long countByWorkOrderRegDateBetween(LocalDateTime startDay, LocalDateTime endDay);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN TRUE ELSE FALSE END " +
            "FROM WorkOrder w " +
            "JOIN SalesOrderItem soi ON w.salesOrder.salesOrderSeq = soi.salesOrder.salesOrderSeq " +
            "WHERE soi.salesOrderItemSeq = :salesOrderItemSeq")
    boolean existsBySalesOrderItemSeq(@Param("salesOrderItemSeq") Long salesOrderItemSeq);
}

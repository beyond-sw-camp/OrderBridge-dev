package error.pirate.backend.salesOrder.command.domain.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesOrderItemRepository  extends JpaRepository<SalesOrderItem, Long> {

    Optional<SalesOrderItem> findBySalesOrderItemSeq(Long salesOrderItemSeq);
}

package error.pirate.backend.salesOrder.command.domain.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;

import java.util.Optional;

public interface SalesOrderItemRepository {

    Optional<SalesOrderItem> findBySalesOrderItemSeq(Long salesOrderItemSeq);
    
    SalesOrderItem save(SalesOrderItem salesOrderItem);
}

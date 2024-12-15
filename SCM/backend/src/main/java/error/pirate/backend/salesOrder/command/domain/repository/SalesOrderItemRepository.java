package error.pirate.backend.salesOrder.command.domain.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;

import java.util.ArrayList;
import java.util.Optional;

public interface SalesOrderItemRepository {

    Optional<SalesOrderItem> findBySalesOrderItemSeq(Long salesOrderItemSeq);
    
    SalesOrderItem save(SalesOrderItem salesOrderItem);

    ArrayList<SalesOrderItem> findBySalesOrderSalesOrderSeq(Long salesOrderSeq);

    void delete(SalesOrderItem salesOrderItem);
}

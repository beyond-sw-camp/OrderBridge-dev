package error.pirate.backend.salesOrder.command.domain.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;

public interface SalesOrderItemRepository {
    SalesOrderItem save(SalesOrderItem salesOrderItem);
}

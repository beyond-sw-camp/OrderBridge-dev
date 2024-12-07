package error.pirate.backend.salesOrder.command.domain.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;

public interface SalesOrderRepository {
    SalesOrder findBySalesOrderName(String salesOrderName);
}

package error.pirate.backend.salesOrder.query.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;

public interface SalesOrderQueryRepository {
    SalesOrder findByProductionReceivingSeq(Long productionReceivingSeq);
}

package error.pirate.backend.salesOrder.command.domain.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;

import java.util.Optional;

public interface SalesOrderCommandRepository {
    SalesOrder save(SalesOrder salesOrder);

    Optional<SalesOrder> findById(Long salesOrderSeq);
}

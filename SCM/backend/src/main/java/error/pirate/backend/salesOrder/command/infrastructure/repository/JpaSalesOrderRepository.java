package error.pirate.backend.salesOrder.command.infrastructure.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSalesOrderRepository extends SalesOrderCommandRepository, JpaRepository<SalesOrder, Long> {
}

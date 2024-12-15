package error.pirate.backend.salesOrder.command.infrastructure.repository;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSalesOrderItemRepository extends SalesOrderItemRepository, JpaRepository<SalesOrderItem, Long> {
}

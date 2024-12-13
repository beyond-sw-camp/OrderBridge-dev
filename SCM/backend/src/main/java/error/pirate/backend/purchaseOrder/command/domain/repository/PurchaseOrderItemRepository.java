package error.pirate.backend.purchaseOrder.command.domain.repository;

import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {
}

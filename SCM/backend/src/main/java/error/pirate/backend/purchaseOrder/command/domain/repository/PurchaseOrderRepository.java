package error.pirate.backend.purchaseOrder.command.domain.repository;

import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository  extends JpaRepository<PurchaseOrder, Long> {
}

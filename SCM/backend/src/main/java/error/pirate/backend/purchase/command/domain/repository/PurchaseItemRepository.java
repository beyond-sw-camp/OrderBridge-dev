package error.pirate.backend.purchase.command.domain.repository;

import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}

package error.pirate.backend.purchase.command.domain.repository;

import error.pirate.backend.purchase.command.domain.aggregate.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}

package error.pirate.backend.productionReceiving.command.domain.repository;


import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionReceivingItemRepository extends JpaRepository<ProductionReceivingItem, Long> {
}

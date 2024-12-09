package error.pirate.backend.productionReceiving.command.domain.repository;


import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductionReceivingItemRepository extends JpaRepository<ProductionReceivingItem, Long> {

    List<ProductionReceivingItem> findAllByProductionReceiving(ProductionReceiving productionReceiving);
}

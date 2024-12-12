package error.pirate.backend.productionReceiving.command.domain.repository;


import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingItem;
import error.pirate.backend.productionReceiving.query.repository.ProductionReceivingItemQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductionReceivingItemRepository extends JpaRepository<ProductionReceivingItem, Long>, ProductionReceivingItemQueryRepository {

    List<ProductionReceivingItem> findAllByProductionReceiving(ProductionReceiving productionReceiving);
}

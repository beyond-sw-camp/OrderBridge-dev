package error.pirate.backend.productionReceiving.query.repository;

import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingItemQueryDTO;

import java.util.List;

public interface ProductionReceivingItemQueryRepository {
    List<ProductionReceivingItemQueryDTO> findAllByProductionReceivingSeq(Long productionReceivingSeq);
}

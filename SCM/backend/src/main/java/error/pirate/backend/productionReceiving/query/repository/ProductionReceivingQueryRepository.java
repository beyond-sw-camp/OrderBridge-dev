package error.pirate.backend.productionReceiving.query.repository;

import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingDTO;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductionReceivingQueryRepository {
    Page<ProductionReceivingDTO> findAllByFilter(ProductionReceivingListRequest request, Pageable pageable);
}

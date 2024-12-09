package error.pirate.backend.productionReceiving.query.repository;

import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListDTO;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductionReceivingQueryRepository {
    Page<ProductionReceivingListDTO> findAllByFilter(ProductionReceivingListRequest request, Pageable pageable);
}

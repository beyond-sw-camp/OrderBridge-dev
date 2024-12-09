package error.pirate.backend.productionReceiving.query.service;

import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingItemRepository;
import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingRepository;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingDTO;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListRequest;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductionReceivingQueryService {

    private final ProductionReceivingRepository productionReceivingRepository;

    public ProductionReceivingListResponse readProductionReceivingList(ProductionReceivingListRequest request, Pageable pageable) {
        Page<ProductionReceivingDTO> productionReceivingList = productionReceivingRepository.findAllByFilter(request, pageable);
        return new ProductionReceivingListResponse(productionReceivingList);
    }
}

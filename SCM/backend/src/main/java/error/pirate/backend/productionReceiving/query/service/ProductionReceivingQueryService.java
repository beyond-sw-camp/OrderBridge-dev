package error.pirate.backend.productionReceiving.query.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingRepository;
import error.pirate.backend.productionReceiving.query.dto.*;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import error.pirate.backend.warehouse.query.dto.WarehouseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionReceivingQueryService {

    private final ProductionReceivingRepository productionReceivingRepository;
    private final WarehouseRepository warehouseRepository;
    private final ModelMapper modelMapper;

    public ProductionReceivingListResponse readProductionReceivingList(ProductionReceivingListRequest request, Pageable pageable) {
        Page<ProductionReceivingListDTO> productionReceivingList = productionReceivingRepository.findAllByFilter(request, pageable);
        return new ProductionReceivingListResponse(productionReceivingList);
    }

    public ProductionReceivingResponse readProductionReceiving(Long productionReceivingSeq) {
        ProductionReceiving productionReceiving = productionReceivingRepository.findById(productionReceivingSeq).orElseThrow(() -> new CustomException(ErrorCodeType.PRODUCTION_RECEIVING_NOT_FOUND));
        Warehouse productionWarehouse = warehouseRepository.findById(productionReceiving.getProductionWarehouse().getWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));
        Warehouse storeWarehouse = warehouseRepository.findById(productionReceiving.getStoreWarehouse().getWarehouseSeq()).orElseThrow(() -> new CustomException(ErrorCodeType.WAREHOUSE_NOT_FOUND));

        return new ProductionReceivingResponse(
                modelMapper.map(productionReceiving, ProductionReceivingDTO.class),
                modelMapper.map(productionWarehouse, WarehouseDTO.class),
                modelMapper.map(storeWarehouse, WarehouseDTO.class)
        );
    }
}

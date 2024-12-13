package error.pirate.backend.productionReceiving.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingItemRepository;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionReceivingQueryService {

    private final ProductionReceivingRepository productionReceivingRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductionReceivingItemRepository productionReceivingItemRepository;
    private final ModelMapper modelMapper;
    private final ExcelDownLoad excelDownBody;

    public ProductionReceivingListResponse readProductionReceivingList(ProductionReceivingListRequest request, Pageable pageable) {
        Page<ProductionReceivingListDTO> productionReceivingList = productionReceivingRepository.findAllByFilter(request, pageable);

        // 각 생산입고의 아이템을 조회
        for(ProductionReceivingListDTO dto : productionReceivingList) {
            dto.setProductionReceivingItemList(productionReceivingItemRepository.findAllByProductionReceivingSeq(dto.getProductionReceivingSeq()));
        }

        // Enum Type을 리스트로 변환
        List<ProductionReceivingStatus.ProductionReceivingStatusResponse> productionReceivingStatusList =
                Arrays.stream(ProductionReceivingStatus.class.getEnumConstants()).map(key ->
                        new ProductionReceivingStatus.ProductionReceivingStatusResponse(
                                key.toString(), ProductionReceivingStatus.valueOf(key.toString())
                        )).toList();

        return new ProductionReceivingListResponse(productionReceivingList, productionReceivingStatusList);
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

    public byte[] productionReceivingExcelDown(ProductionReceivingListRequest request, Pageable pageable) {
        Page<ProductionReceivingListDTO> productionReceivingList = productionReceivingRepository.findAllByFilter(request, pageable);

        String[] headers = {"생산입고명", "생산입고 품목", "생산공장명", "보관창고명", "입고일", "상태"};
        String[][] excel = new String[productionReceivingList.getSize()][headers.length];

        for(int i = 0; i < productionReceivingList.getContent().size(); i++) {
            ProductionReceivingListDTO dto = productionReceivingList.getContent().get(i);
            dto.setProductionReceivingItemList(productionReceivingItemRepository.findAllByProductionReceivingSeq(dto.getProductionReceivingSeq()));

            excel[i][0] = dto.getProductionReceivingName(); // 생산입고 명
            excel[i][1] = dto.getProductionReceivingItemList()
                    .stream()
                    .map(ProductionReceivingItemQueryDTO::getItemName)
                    .collect(Collectors.joining(", "));// 생산입고 품목
            excel[i][2] = dto.getProductionWarehouseName();       // 생산창고
            excel[i][3] = dto.getStoreWarehouseName();            // 보관창고
            excel[i][4] = dto.getProductReceivingRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // 등록일
            excel[i][5] = String.valueOf(dto.getProductionReceivingStatus());
        }

        return excelDownBody.excelDownBody(excel, headers, "생산입고");
    }
}

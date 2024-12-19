package error.pirate.backend.productionReceiving.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceiving;
import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingItemRepository;
import error.pirate.backend.productionReceiving.command.domain.repository.ProductionReceivingRepository;
import error.pirate.backend.productionReceiving.query.dto.*;
import error.pirate.backend.productionReceiving.query.mapper.ProductionReceivingMapper;
import error.pirate.backend.warehouse.command.domain.repository.WarehouseRepository;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrder;
import error.pirate.backend.workOrder.command.domain.repository.WorkOrderRepository;
import error.pirate.backend.workOrder.query.dto.WorkOrderListDTO;
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
    private final ProductionReceivingMapper productionReceivingMapper;
    private final WorkOrderRepository workOrderRepository;

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
        List<ProductionReceivingItemQueryDTO> productionReceivingItemList = productionReceivingItemRepository.findAllByProductionReceivingSeq(productionReceivingSeq);
        List<WorkOrder> workOrders = workOrderRepository.findByProductionReceiving(productionReceiving);

        List<WorkOrderListDTO> workOrderList = new ArrayList<>();
        for(WorkOrder workOrder : workOrders) {
            workOrderList.add(modelMapper.map(workOrder, WorkOrderListDTO.class));
        }
        return new ProductionReceivingResponse(
                modelMapper.map(productionReceiving, ProductionReceivingDTO.class),
                productionReceivingItemList,
                workOrderList
                );
    }

    public byte[] productionReceivingExcelDown(ProductionReceivingListRequest request, Pageable pageable) {
        Page<ProductionReceivingListDTO> productionReceivingList = productionReceivingRepository.findAllByFilter(request, pageable);

        String[] headers = {"생산입고명", "생산입고 품목", "입고일", "상태"};
        String[][] excel = new String[productionReceivingList.getSize()][headers.length];

        for(int i = 0; i < productionReceivingList.getContent().size(); i++) {
            ProductionReceivingListDTO dto = productionReceivingList.getContent().get(i);
            dto.setProductionReceivingItemList(productionReceivingItemRepository.findAllByProductionReceivingSeq(dto.getProductionReceivingSeq()));

            excel[i][0] = dto.getProductionReceivingName(); // 생산입고 명
            excel[i][1] = dto.getProductionReceivingItemList()
                    .stream()
                    .map(ProductionReceivingItemQueryDTO::getItemName)
                    .collect(Collectors.joining(", "));// 생산입고 품목
            excel[i][2] = dto.getProductionReceivingReceiptDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // 등록일
            excel[i][3] = String.valueOf(dto.getProductionReceivingStatus());
        }

        return excelDownBody.excelDownBody(excel, headers, "생산입고");
    }

    public List<ProductionReceivingSituationResponse> readProductionReceivingSituation(ProductionReceivingSituationRequest request) {
        return productionReceivingMapper.findProductionReceivingSituationByFilter(request);
    }

    public byte[] productionReceivingSituationExcelDown(ProductionReceivingSituationRequest request) {

        List<ProductionReceivingSituationResponse> productionReceivingSituationList
                = productionReceivingMapper.findProductionReceivingSituationByFilter(request);

        String[] headers = {"번호", "생산입고일", "생산입고명", "총금액", "거래처명", "비고"};
        String[][] excel = new String[productionReceivingSituationList.size()][headers.length];

        for(int i = 0; i < productionReceivingSituationList.size(); i++) {
            ProductionReceivingSituationResponse dto = productionReceivingSituationList.get(i);
            if(dto.getProductionReceivingRegDate() == null) {
                boolean isNull = (dto.getProductionReceivingRegMonth() == null);

                excel[i][0] = isNull ? "" : String.valueOf(i+1); // 번호
                excel[i][1] = isNull ? "" : dto.getProductionReceivingRegMonth(); // 생산입고월
                excel[i][2] = isNull ? "총합" : "-";       // 생산입고명
                excel[i][3] = dto.getProductionReceivingSum() + " ￦";            // 총금액
                excel[i][4] = "-";
                excel[i][5] = "-";
            } else {
                excel[i][0] = String.valueOf(i+1); // 번호
                excel[i][1] = dto.getProductionReceivingRegDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // 생산입고일
                excel[i][2] = dto.getProductionReceivingName();       // 생산입고명
                excel[i][3] = dto.getProductionReceivingExtendedPrice() + " ￦";            // 총금액
                excel[i][4] = dto.getClientName(); // 거래처명
                excel[i][5] = dto.getProductionReceivingNote(); // 비고
            }
        }

        return excelDownBody.excelDownBody(excel, headers, "생산입고 현황");
    }
}

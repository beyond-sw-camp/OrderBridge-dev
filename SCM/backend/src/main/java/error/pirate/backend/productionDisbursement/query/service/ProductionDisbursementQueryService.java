package error.pirate.backend.productionDisbursement.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import error.pirate.backend.productionDisbursement.query.dto.*;
import error.pirate.backend.productionDisbursement.query.mapper.ProductionDisbursementMapper;
import error.pirate.backend.salesOrder.query.dto.SalesOrderResponse;
import error.pirate.backend.shippingInstruction.query.dto.ShippingInstructionItemDTO;
import error.pirate.backend.workOrder.query.dto.WorkOrderDetailDTO;
import error.pirate.backend.workOrder.query.dto.WorkOrderItemDTO;
import error.pirate.backend.workOrder.query.dto.WorkOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductionDisbursementQueryService {

    private final ProductionDisbursementMapper productionDisbursementMapper;
    private final ExcelDownLoad excelDownBody;

    /* 생산불출 목록 조회 */
    @Transactional
    public ProductionDisbursementListResponse readProductionDisbursementList(String factoryName,
                                                                             List<ProductionDisbursementStatus> productionDisbursementStatus,
                                                                             LocalDate startDate,
                                                                             LocalDate endDate,
                                                                             Integer page,
                                                                             Integer size) {
        log.info("-------------- 생산불출 목록조회 서비스 진입 :목록조회 필터링 조건 - " +
                "factoryName : {}, productionDisbursementStatus:{}, startDate:{}, endDate:{}, page:{}, size: {} --------------",
                factoryName, productionDisbursementStatus, startDate, endDate, page, size);

        // 날짜 변환
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        // null 체크 및 날짜 유효성 검증
        if (startDateTime != null && endDateTime != null) {
            if (startDateTime.isAfter(endDateTime)) {
                throw new CustomException(ErrorCodeType.INVALID_DATE_RANGE);
            }
        }

        // 페이지 설정
        int offset = (page - 1) * size;

        // 상태 목록
        List<ProductionDisbursementStatus> statusList = productionDisbursementStatus;
        log.info("상태: {}",statusList);

        // 생산불출 목록 조회
        List<ProductionDisbursementListDTO> productionDisbursementList
                = productionDisbursementMapper.readProductionDisbursementList(factoryName, statusList, startDateTime, endDateTime, offset, size);

        // enum 상태와 함께 응답
        List<ProductionDisbursementStatus.ProductionDisbursementStatusResponse> productionDisbursementStatusResponse
                = Arrays.stream(ProductionDisbursementStatus.class.getEnumConstants())
                .map(key -> new ProductionDisbursementStatus.ProductionDisbursementStatusResponse(
                        key.toString(), ProductionDisbursementStatus.valueOf(key.toString())
                )).toList();

        // 총 개수
        long totalItems = productionDisbursementMapper.countProductionDisbursementList(factoryName, statusList, startDateTime, endDateTime);
        // 총 페이지 수
        int totalPages = (int) Math.ceil((double) totalItems / size);

        log.info("-------------- readProductionDisbursementList 완료 - 페이지에 조회된 생산불출 수 : {}, 총 생산불출 수 : {}, 총 페이지 수 : {} --------------",
                productionDisbursementList.size(), totalItems, totalPages);

        return ProductionDisbursementListResponse.builder()
                .productionDisbursementList(productionDisbursementList)
                .productionDisbursementStatusList(productionDisbursementStatusResponse)
                .currentPage(page)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .build();
    }

    /* 생산불출 상세조회 */
    public ProductionDisbursementResponse readProductionDisbursement(Long productionDisbursementSeq) {
        log.info("-------------- 생산불출 상세조회 서비스 진입 - productionDisbursementSeq: {} --------------", productionDisbursementSeq);

        // 품목 제외 생산불출 조회
        ProductionDisbursementDetailDTO productionDisbursementDetail = productionDisbursementMapper.readProductionDisbursement(productionDisbursementSeq);
        if(productionDisbursementDetail == null) {
            throw new CustomException(ErrorCodeType.PRODUCTION_DISBURSEMENT_NOT_FOUND);
        }

        // 해당 품목 조회
        List<ProductionDisbursementItemDTO> itemList = productionDisbursementMapper.readProductionDisbursementItem(productionDisbursementSeq);

        return ProductionDisbursementResponse.builder()
                .productionDisbursementDetail(productionDisbursementDetail)
                .itemList(itemList)
                .build();
    }

    /* 생산불출 목록 다운로드 */
    public byte[] readProductionDisbursementExcel(LocalDate startDate, LocalDate endDate, String factoryName, List<ProductionDisbursementStatus> productionDisbursementStatus) {
        return excelDownBody.writeCells(
                new String[] {"생산불출명", "품목명", "불출수량", "원자재창고", "생산공장명", "불출일", "상태"},
                productionDisbursementMapper.downloadProductionDisbursementListExcel(startDate, endDate, factoryName, productionDisbursementStatus)
        );
    }

}

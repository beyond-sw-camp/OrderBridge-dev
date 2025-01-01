package error.pirate.backend.salesOrder.query.service;

import error.pirate.backend.common.ExcelDownLoad;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.query.dto.*;
import error.pirate.backend.salesOrder.query.mapper.SalesOrderMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderQueryService {

    private final ModelMapper modelMapper;
    private final SalesOrderMapper salesOrderMapper;
    private final ExcelDownLoad excelDownBody;

    // 주문서 목록 조회
    public SalesOrderListResponse readSalesOrderList(
            Integer page, Integer size,
            LocalDate startDate, LocalDate endDate,
            String clientName, List<SalesOrderStatus> salesOrderStatus) {

        // 주문서 목록 조회
        List<SalesOrderListItemDTO> salesOrderList = salesOrderMapper.selectSalesOrderList(
                (page - 1) * size, size, startDate, endDate, clientName, salesOrderStatus);

        // 주문서 목옥 개수 조회
        int totalSalesOrder = salesOrderMapper.countSalesOrderList(startDate, endDate, clientName, salesOrderStatus);

        return new SalesOrderListResponse(
                salesOrderList,
                page,
                (int) Math.ceil((double) totalSalesOrder / size),
                totalSalesOrder
        );
    }

    // 주문서 상세 조회
    public SalesOrderResponse readSalesOrder(Long salesOrderSeq) {

        // 주문서 상세 조회
        SalesOrderResponse response = modelMapper.map(
                salesOrderMapper.selectSalesOrder(salesOrderSeq), SalesOrderResponse.class);

        // 주문서 품목 목록 조회
        response.setSalesOrderItem(salesOrderMapper.selectSalesOrderItem(salesOrderSeq));

        return response;
    }

    // 주문서 현황 조회
    public SalesOrderSituationResponse readSalesOrderSituation(
            LocalDate startDate, LocalDate endDate, String clientName) {

        // 주문서 현황 조회
        return new SalesOrderSituationResponse(salesOrderMapper.selectSalesOrderSituation(
                startDate, endDate, clientName));
    }

    // 주문서 품목 값 확인
    public List<SalesOrderItemCheckDTO> salesOrderItemCheck(Long quotationSeq) {
        return salesOrderMapper.sumSalesOrderItemValue(quotationSeq);
    }

    // 주문서 목록 엑셀 다운로드
    public byte[] readSalesOrderExcel(
            LocalDate startDate, LocalDate endDate, String clientName, List<SalesOrderStatus> salesOrderStatus) {

        return excelDownBody.writeCells(
                new String[] {"등록", "수정", "이름", "상태", "주문일", "납기일", "총 수량", "총 가격", "비고"},
                salesOrderMapper.selectSalesOrderExcel(startDate, endDate, clientName, salesOrderStatus)
        );
    }

    // 작업지시가 등록된 주문서 물품 조회
    public List<Long> readRegisteredItems(Long salesOrderSeq) {
        return salesOrderMapper.selectRegisteredItemSeqsBySalesOrderSeq(salesOrderSeq);
    }

    // 주문서(품목)에 대한 재고 상태 조회
    public List<SalesOrderItemStockStatusResponse> readSalesOrderItemStock(Long salesOrderSeq) {

        // 현재 시간 기준으로 만료되지 않은 재고 확인
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 주문서에 품목이 있는지 확인
        List<SalesOrderItem> salesOrderItems = salesOrderMapper.findItemsBySalesOrderSeq(salesOrderSeq);
        if (salesOrderItems.isEmpty()) {
            throw new CustomException(ErrorCodeType.SALES_ORDER_ITEM_NOT_FOUND);
        }

        // 품목이 있다면 재고 상태 조회
        List<SalesOrderItemStockStatusResponse> stockStatusList =
                salesOrderMapper.selectSalesOrderItemStockStatus(salesOrderSeq, currentDateTime);

        for (SalesOrderItemStockStatusResponse item : stockStatusList) {
            item.calculateStockStatus(); // DTO의 재고 상태 계산
        }

        return stockStatusList;
    }
}

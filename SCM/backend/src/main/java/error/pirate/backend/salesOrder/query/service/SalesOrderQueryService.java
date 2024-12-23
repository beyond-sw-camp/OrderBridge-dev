package error.pirate.backend.salesOrder.query.service;

import error.pirate.backend.salesOrder.query.dto.*;
import error.pirate.backend.salesOrder.query.mapper.SalesOrderMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderQueryService {

    private final ModelMapper modelMapper;
    private final SalesOrderMapper salesOrderMapper;

    // 주문서 목록 조회
    public SalesOrderListResponse readSalesOrderList(
            Integer page, Integer size,
            LocalDate startDate, LocalDate endDate,
            String clientName, String salesOrderStatus) {

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

    // 작업지시가 등록된 주문서 물품 조회
    public List<Long> readRegisteredItems(Long salesOrderSeq) {
        return salesOrderMapper.findRegisteredItemSeqsBySalesOrderSeq(salesOrderSeq);
    }
}

package error.pirate.backend.salesOrder.query.service;

import error.pirate.backend.salesOrder.query.dto.SalesOrderListItemDTO;
import error.pirate.backend.salesOrder.query.dto.SalesOrderListResponse;
import error.pirate.backend.salesOrder.query.mapper.SalesOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderQueryService {

    private final SalesOrderMapper salesOrderMapper;

    // 주문서 목록 조회
    public SalesOrderListResponse getSalesOrderList(
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
}

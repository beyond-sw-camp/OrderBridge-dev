package error.pirate.backend.salesOrder.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderRepository;
import error.pirate.backend.salesOrder.query.dto.SalesOrderItemCheckDTO;
import error.pirate.backend.salesOrder.query.service.SalesOrderQueryService;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOrderDomainService {
    
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderQueryService salesOrderQueryService;

    /* 주문서 시퀀스로 주문서 불러오기 */
    public SalesOrder findById(long salesOrderSeq) {
        return salesOrderRepository.findById(salesOrderSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SALES_ORDER_NOT_FOUND));
    }

    /* 상태를 수정하는 로직 */
    public void updateSalesOrderStatus(SalesOrder salesOrder, String status) {
        /* 수정을 위해 엔터티 정보 변경 */
        salesOrder.updateStatus(status);
    }

    // 견적서와 주문서의 품목 수량 비교
    public void validateItem(Long quotationSeq) {
        List<SalesOrderItemCheckDTO> salesOrderItemCheckList =
                salesOrderQueryService.salesOrderItemCheck(quotationSeq);

        for (SalesOrderItemCheckDTO salesOrderItemCheck : salesOrderItemCheckList) {
            if (salesOrderItemCheck.getRemainingQuantity() < 0) {
                throw new CustomException(ErrorCodeType.SALES_ORDER_ITEM_NOT_MATCH);
            }
        }
    }

    // 주문서 상태 확인
    public void checkSalesOrderStatus(SalesOrderStatus salesOrderStatus) {
        /* 결재후가 아니라면 불가*/
        if (!salesOrderStatus.equals(SalesOrderStatus.AFTER)) {
            throw new CustomException(ErrorCodeType.SALES_ORDER_STATE_BAD_REQUEST);
        }
    }

    /* 결재후로 변경 */
    public void updateSalesOrderStatus(Long salesOrderSeq, SalesOrderStatus salesOrderStatus) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.SALES_ORDER_NOT_FOUND));
        salesOrder.updateSalesOrderStatus(salesOrderStatus);
    }

}

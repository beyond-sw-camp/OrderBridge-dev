package error.pirate.backend.purchaseOrder.command.application.service;

import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderCreateRequest;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.purchaseOrder.command.domain.service.PurchaseOrderDomainService;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderApplicationService {

    private final PurchaseOrderDomainService purchaseOrderDomainService;

    private final SalesOrderDomainService salesOrderDomainService;

    @Transactional
    public void createPurchaseOrder(PurchaseOrderCreateRequest request) {
        // 발주서명은 uuid 생성해서 넣어도 되는지?
        SalesOrder salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
        if(salesOrder.getSalesOrderStatus() == SalesOrderStatus.BEFORE) {
            purchaseOrderDomainService.createPurchaseOrder(request);
        }
    }

    @Transactional
    public void updatePurchaseOrder(PurchaseOrderUpdateRequest request) {
        SalesOrder salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
        // 생산 중이 아닐 때만 수정 가능
        if(salesOrder.getSalesOrderStatus() != SalesOrderStatus.PRODUCTION) {
            purchaseOrderDomainService.updatePurchaseOrder(request);
        }
    }

    @Transactional
    public void deletePurchaseOrder(Long purchaseOrderSeq, Long salesOrderSeq) {
        SalesOrder salesOrder = salesOrderDomainService.findById(salesOrderSeq);
        // 생산 중이 아닐 때만 삭제 가능
        if(salesOrder.getSalesOrderStatus() != SalesOrderStatus.PRODUCTION) {
            purchaseOrderDomainService.deletePurchaseOrder(purchaseOrderSeq);
        }
    }

    public void updatePurchaseOrderComplete(Long purchaseOrderSeq) {
    }

    public void updatePurchaseOrderRefusal(Long purchaseOrderSeq) {
    }

}

package error.pirate.backend.purchaseOrder.command.application.service;

import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderCreateRequest;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.purchaseOrder.command.domain.service.PurchaseOrderDomainService;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderApplicationService {

    private final PurchaseOrderDomainService purchaseOrderDomainService;
    private final SalesOrderDomainService salesOrderDomainService;


    public void createPurchaseOrder(PurchaseOrderCreateRequest request) {
        // 발주서명은 어떻게 넣을건지?
        SalesOrder salesOrder = salesOrderDomainService.findById(request.getSalesOrderSeq());
    }

    public void updatePurchaseOrder(PurchaseOrderUpdateRequest request) {
    }

    public void deletePurchaseOrder(Long purchaseOrderSeq) {
        // 해당 발주서가 생산 중일 경우 삭제 안됨
        // 생산 중인지 확인 필요
    }


    public void updatePurchaseOrderComplete(Long purchaseOrderSeq) {
    }

    public void updatePurchaseOrderRefusal(Long purchaseOrderSeq) {
    }

}

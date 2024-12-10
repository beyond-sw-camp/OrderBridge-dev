package error.pirate.backend.purchaseOrder.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderCreateRequest;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import error.pirate.backend.purchaseOrder.command.domain.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderDomainService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final ModelMapper modelMapper;

    public void createPurchaseOrder(PurchaseOrderCreateRequest request) {
        PurchaseOrder purChaseOrder = modelMapper.map(request, PurchaseOrder.class);
        purchaseOrderRepository.save(purChaseOrder);
    }

    public void updatePurchaseOrder(PurchaseOrderUpdateRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(request.getPurchaseOrderSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND));
        purchaseOrder.updatePurchaseOrder(request); // 수정에도 주문서랑 거래처, 품목 변경할수있는지?
    }

    public void deletePurchaseOrder(Long purchaseOrderSeq) {
        purchaseOrderRepository.deleteById(purchaseOrderSeq);
    }


}

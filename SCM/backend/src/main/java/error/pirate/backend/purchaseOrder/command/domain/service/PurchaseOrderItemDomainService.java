package error.pirate.backend.purchaseOrder.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderCreateRequest;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderItemDto;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderItem;
import error.pirate.backend.purchaseOrder.command.domain.repository.PurchaseOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderItemDomainService {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    public void createPurchaseOrderItem(List<PurchaseOrderItem> items) {
        purchaseOrderItemRepository.saveAll(items);
    }

    public void updatePurchaseOrderItem(PurchaseOrderItemDto purchaseOrderItem) {
        PurchaseOrderItem purChaseOrderItemResponse = purchaseOrderItemRepository.findById(purchaseOrderItem.getPurchaseOrderItemSeq())
                .orElseThrow( () -> new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND));
        purChaseOrderItemResponse.updateOrderInfo(purchaseOrderItem.getPurchaseOrderItemQuantity(), purchaseOrderItem.getPurchaseOrderItemPrice(), purchaseOrderItem.getPurchaseOrderItemNote());
    }

}

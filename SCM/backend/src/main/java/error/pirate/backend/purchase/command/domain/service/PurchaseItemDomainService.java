package error.pirate.backend.purchase.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.purchase.command.application.dto.PurchaseItemDto;
import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseItem;
import error.pirate.backend.purchase.command.domain.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseItemDomainService {

    private final PurchaseItemRepository purchaseItemRepository;

    public void createPurchaseItem(List<PurchaseItem> items) {
        purchaseItemRepository.saveAll(items);
    }


    public void updatePurchaseItem(PurchaseItemDto purchaseItem) {
        PurchaseItem purChaseItemResponse = purchaseItemRepository.findById(purchaseItem.getPurchaseItemSeq())
                .orElseThrow( () -> new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND));

        purChaseItemResponse.updateOrderInfo(purchaseItem.getPurchaseItemQuantity(), purchaseItem.getPurchaseItemPrice(), purchaseItem.getPurchaseItemNote(), purchaseItem.getPurchaseItemReceiptDate());
    }

}

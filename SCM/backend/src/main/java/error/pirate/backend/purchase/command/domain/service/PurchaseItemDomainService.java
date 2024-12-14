package error.pirate.backend.purchase.command.domain.service;

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

}

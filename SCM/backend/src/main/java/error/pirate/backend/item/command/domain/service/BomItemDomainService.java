package error.pirate.backend.item.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import error.pirate.backend.item.command.domain.repository.BomItemRepository;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BomItemDomainService {

    private final BomItemRepository bomItemRepository;
    private final ItemRepository itemRepository;
    private final ItemInventoryDomainService itemInventoryDomainService;

    // 부모 품목(Item)에 연결된 BOM 항목 조회
    public List<BomItem> findAllByParentItem(Item parentItem) {
        return bomItemRepository.findAllByParentItem(parentItem);
    }

    // BOM 항목을 통해 재고 검증 및 차감
    public void validateAndDeductStock(Item parentItem, int indicatedQuantity) {
        List<BomItem> bomItems = findAllByParentItem(parentItem);

        if (bomItems == null || bomItems.isEmpty()) {
            throw new CustomException(ErrorCodeType.BOM_ITEM_NOT_FOUND);
        }

        List<Item> childItems = new ArrayList<>();
        List<Integer> requiredQuantities = new ArrayList<>();

        for (BomItem bomItem : bomItems) {
            // BOM 하위 품목 조회
            Long childItemSeq = bomItem.getChildItem().getItemSeq();
            int requiredQuantity = bomItem.getBomChildItemQuantity() * indicatedQuantity;

            Item childItem = itemRepository.findById(childItemSeq)
                    .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));

            childItems.add(childItem);
            requiredQuantities.add(requiredQuantity);
        }

        // 재고 차감
        itemInventoryDomainService.updateItemQuantity(childItems, requiredQuantities);
    }

}

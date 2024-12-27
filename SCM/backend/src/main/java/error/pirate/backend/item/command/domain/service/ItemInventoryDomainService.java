package error.pirate.backend.item.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemInventory;
import error.pirate.backend.item.command.domain.repository.ItemInventoryRepository;
import error.pirate.backend.item.query.dto.ItemInventoryDTO;
import error.pirate.backend.purchase.command.application.dto.PurchaseCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemInventoryDomainService {

    private final ItemInventoryRepository itemInventoryRepository;

    private final ModelMapper modelMapper;

    public void checkInventoryForBomItems(List<BomItem> bomItems, int orderQuantity) {
        for (BomItem bomItem : bomItems) {
            // BOM 품목 별 필요한 수량 계산
            int requiredQuantity = bomItem.getBomChildItemQuantity() * orderQuantity;

            // 재고조회
            int availableQuantity = itemInventoryRepository.countByItemAndItemInventoryExpirationDateAfter(bomItem.getChildItem(), LocalDateTime.now());

            log.debug("BOM Item: {}, Required Quantity: {}, Available Quantity: {}",
                    bomItem.getChildItem().getItemSeq(), requiredQuantity, availableQuantity);

            // 재고 부족 시 예외 처리
            if (availableQuantity < requiredQuantity) {
                throw new CustomException(ErrorCodeType.OUT_OF_STOCK_ERROR);
            }
        }
    }

    public void checkInventoryStockForBomItems(List<BomItem> bomItems, int orderQuantity) {
        for (BomItem bomItem : bomItems) {
            // BOM 품목 별 필요한 수량 계산
            int requiredQuantity = bomItem.getBomChildItemQuantity() * orderQuantity;

            // 재고조회
            int availableQuantity = itemInventoryRepository.sumRemainAmountByItemAndItemInventoryExpirationDateAfter(bomItem.getChildItem(), LocalDateTime.now());

            log.debug("BOM Item: {}, Required Quantity: {}, Available Quantity: {}",
                    bomItem.getChildItem().getItemSeq(), requiredQuantity, availableQuantity);

            // 재고 부족 시 예외 처리
            if (availableQuantity < requiredQuantity) {
                throw new CustomException(ErrorCodeType.OUT_OF_STOCK_ERROR);
            }
        }
    }

    // 재고 수량 업데이트
    @Transactional
    public void updateItemQuantity(List<Item> itemList, List<Integer> itemQuantityList) {
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            int remainingQuantity = itemQuantityList.get(i); // 소진해야 할 수량

            // 1. 유효한 ItemInventory 조회
            List<ItemInventory> inventoryList = itemInventoryRepository.findValidInventoriesByItemSeq(item.getItemSeq());

            // 2. 수량 분배
            for (ItemInventory inventory : inventoryList) {
                if (remainingQuantity <= 0) break;

                int currentAmount = inventory.getItemInventoryRemainAmount();
                if (currentAmount > 0) {
                    int deduction = Math.min(currentAmount, remainingQuantity); // 감소할 수량
                    inventory.updateItemInventoryRemainAmount(currentAmount - deduction);
                    remainingQuantity -= deduction;
                }
            }

            // 3. 남은 수량 확인 (optional)
            if (remainingQuantity > 0) {
                throw new CustomException(ErrorCodeType.OUT_OF_STOCK_ERROR);
            }
            // 변경 사항 저장은 @Transactional에 의해 자동 처리됨
        }
    }

    public void createPurchaseItem(List<Item> items, PurchaseCreateRequest purchaseItems) {
        List<ItemInventory> itemList = new ArrayList<>();

        if(ObjectUtils.isNotEmpty(items)) {
            for(Item item : items) {
                ItemInventoryDTO.createPurchaseItem itemInventoryDTO = ItemInventoryDTO.createPurchaseItem.builder()
                                .item(item)
                                .itemInventoryQuantityReceived(
                                        purchaseItems.getPurchaseItemDtoList().stream()
                                                .filter(purchaseItem -> purchaseItem.getItemSeq().equals(item.getItemSeq()))
                                                .findFirst().get().getPurchaseItemQuantity())
                                .itemInventoryRemainAmount(
                                        purchaseItems.getPurchaseItemDtoList().stream()
                                                .filter(purchaseItem -> purchaseItem.getItemSeq().equals(item.getItemSeq()))
                                                .findFirst().get().getPurchaseItemQuantity())
                                .itemInventoryReceiptDate(
                                        purchaseItems.getPurchaseItemDtoList().stream()
                                                .filter(purchaseItem -> purchaseItem.getItemSeq().equals(item.getItemSeq()))
                                                .findFirst().get().getPurchaseItemReceiptDate())
                                .itemInventoryExpirationDate(
                                        purchaseItems.getPurchaseItemDtoList().stream()
                                                .filter(purchaseItem -> purchaseItem.getItemSeq().equals(item.getItemSeq()))
                                                .findFirst().get()
                                                .getPurchaseItemReceiptDate().plusHours(item.getItemExpirationHour()))
                                .build();

                ItemInventory newItem = modelMapper.map(itemInventoryDTO, ItemInventory.class);
                itemList.add(newItem);
            }
        }
        itemInventoryRepository.saveAll(itemList);
    }

}

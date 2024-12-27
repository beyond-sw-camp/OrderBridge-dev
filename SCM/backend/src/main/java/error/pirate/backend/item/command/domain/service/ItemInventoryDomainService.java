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

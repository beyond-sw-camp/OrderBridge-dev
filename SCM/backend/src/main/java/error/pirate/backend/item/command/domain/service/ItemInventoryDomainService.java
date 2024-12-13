package error.pirate.backend.item.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.BomItem;
import error.pirate.backend.item.command.domain.repository.ItemInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemInventoryDomainService {

    private final ItemInventoryRepository itemInventoryRepository;

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
}

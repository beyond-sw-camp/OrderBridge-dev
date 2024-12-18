package error.pirate.backend.shippingInstruction.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderItemRepository;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionItemRequest;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionRequest;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstruction;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionItem;
import error.pirate.backend.shippingInstruction.command.domain.repository.ShippingInstructionItemRepository;
import error.pirate.backend.shippingInstruction.command.mapper.ShippingInstructionItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingInstructionItemDomainService {

    private final ShippingInstructionItemRepository shippingInstructionItemRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;

    /* 도메인 객체를 생성하는 로직 */
    public List<ShippingInstructionItem> createShippingInstructionItemList(
            ShippingInstructionRequest shippingInstructionRequest,
            ShippingInstruction shippingInstruction,
            List<Item> itemList) {

        /* dto to entity */
        List<ShippingInstructionItem> newShippingInstructionItems = ShippingInstructionItemMapper.toEntity(
                shippingInstructionRequest, shippingInstruction, itemList
        );

        return newShippingInstructionItems;
    }

    /* 도메인 객체를 저장하는 로직 */
    public List<ShippingInstructionItem> saveShippingInstructionItem(List<ShippingInstructionItem> newShippingInstructionItemList) {
        return shippingInstructionItemRepository.saveAll(newShippingInstructionItemList);
    }

    /* 출하지시서 번호로 도메인 객체 삭제하는 로직 */
    public void deleteByShippingInstruction(ShippingInstruction newShippingInstruction) {
        shippingInstructionItemRepository.deleteByShippingInstruction(newShippingInstruction);
    }

    // 주문서 품목과 출하지시서 품목에 대한 검증
    public void validateItem(long salesOrderSeq, List<ShippingInstructionItemRequest> shippingInstructionItemRequestList) {
        List<SalesOrderItem> salesOrderItemList =
                salesOrderItemRepository.findBySalesOrderSalesOrderSeq(salesOrderSeq);

        int matchCount = 0;
        for (SalesOrderItem salesOrderItem : salesOrderItemList) {
            for (ShippingInstructionItemRequest shippingInstructionItemRequest : shippingInstructionItemRequestList) {
                if (salesOrderItem.getItem().getItemSeq().equals(shippingInstructionItemRequest.getItemSeq())) {
                    matchCount++;
                }
            }
        }

        if (salesOrderItemList.size() < shippingInstructionItemRequestList.size()
                || matchCount != shippingInstructionItemRequestList.size()) {
            throw new CustomException(ErrorCodeType.SHIPPING_INSTRUCTION_ITEM_NOT_MATCH);
        }
    }
}

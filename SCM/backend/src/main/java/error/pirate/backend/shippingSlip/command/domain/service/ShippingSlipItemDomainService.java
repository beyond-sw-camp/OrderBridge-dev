package error.pirate.backend.shippingSlip.command.domain.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.shippingInstruction.command.application.dto.ShippingInstructionItemRequest;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionItem;
import error.pirate.backend.shippingInstruction.command.domain.repository.ShippingInstructionItemRepository;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipItemRequest;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlipItem;
import error.pirate.backend.shippingSlip.command.domain.repository.ShippingSlipItemRepository;
import error.pirate.backend.shippingSlip.command.mapper.ShippingSlipItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingSlipItemDomainService {

    private final ShippingSlipItemRepository shippingSlipItemRepository;
    private final ShippingInstructionItemRepository shippingInstructionItemRepository;

    /* 도메인 객체를 생성하는 로직 */
    public List<ShippingSlipItem> createShippingSlipItemList(
            ShippingSlipRequest shippingSlipRequest,
            ShippingSlip shippingSlip,
            List<Item> itemList) {

        /* dto to entity */
        List<ShippingSlipItem> newShippingSlipItems = ShippingSlipItemMapper.toEntity(
                shippingSlipRequest, shippingSlip, itemList
        );

        return newShippingSlipItems;
    }

    /* 도메인 객체를 저장하는 로직 */
    public List<ShippingSlipItem> saveShippingSlipItem(List<ShippingSlipItem> newShippingSlipItemList) {
        return shippingSlipItemRepository.saveAll(newShippingSlipItemList);
    }

    // 출하지시서 품목과 출하전표 품목에 대한 검증
    public void validateItem(long shippingInstructionSeq, List<ShippingSlipItemRequest> shippingSlipItemRequestList) {
        List<ShippingInstructionItem> shippingInstructionItemList =
                shippingInstructionItemRepository.findByShippingInstructionShippingInstructionSeq(shippingInstructionSeq);

        int matchCount = 0;
        for (ShippingInstructionItem shippingInstructionItem : shippingInstructionItemList) {
            for (ShippingSlipItemRequest shippingSlipItemRequest : shippingSlipItemRequestList) {
                if (shippingInstructionItem.getItem().getItemSeq().equals(shippingSlipItemRequest.getItemSeq())) {
                    matchCount++;
                }
            }
        }

        if (shippingInstructionItemList.size() < shippingSlipItemRequestList.size()
                || matchCount != shippingSlipItemRequestList.size()) {
            throw new CustomException(ErrorCodeType.SHIPPING_SLIP_ITEM_NOT_MATCH);
        }
    }
}

package error.pirate.backend.shippingSlip.command.mapper;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipItemRequest;
import error.pirate.backend.shippingSlip.command.application.dto.ShippingSlipRequest;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlip;
import error.pirate.backend.shippingSlip.command.domain.aggregate.entity.ShippingSlipItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShippingSlipItemMapper {
    public static List<ShippingSlipItem> toEntity(
            ShippingSlipRequest shippingSlipRequest,
            ShippingSlip shippingSlip,
            List<Item> itemList) {

        List<ShippingSlipItem> shippingSlipItemList = new ArrayList<>();

        // shippingSlipRequest에서 shippingSlipItems 가져오기
        List<ShippingSlipItemRequest> itemDTOList  = shippingSlipRequest.getShippingSlipItems();

        // itemList를 Map으로 변환하여 매핑 속도 향상
        Map<Long, Item> itemMap = itemList.stream()
                .collect(Collectors.toMap(Item::getItemSeq, item -> item));

        // DTO 리스트를 순회하며 ShippingInstructionItem 생성
        for (ShippingSlipItemRequest itemDTO : itemDTOList) {
            Item item = itemMap.get(itemDTO.getItemSeq()); // itemSeq으로 Item 매핑

            if (item == null) {
                // 매핑되지 않은 Item이 있으면 예외 처리
                throw new CustomException(ErrorCodeType.ITEM_NOT_FOUND);
            }

            // ShippingSlipItem 생성 및 리스트에 추가
            ShippingSlipItem shippingSlipItem = ShippingSlipItem.create(
                    shippingSlip, // ShippingSlip 객체
                    item, // Item 객체
                    itemDTO.getShippingSlipItemQuantity(), // 수량
                    itemDTO.getShippingSlipItemNote() // 비고
            );
            shippingSlipItemList.add(shippingSlipItem);
        }

        return shippingSlipItemList;
    }
}

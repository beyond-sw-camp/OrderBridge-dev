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

    private final ModelMapper modelMapper;

    public void createPurchaseOrderItem(Long purchaseOrderSeq, PurchaseOrderCreateRequest request) {
        List<PurchaseOrderItem> items = new ArrayList<>();

        if(ObjectUtils.isNotEmpty(request.getPurchaseOrderItemDtoList())) {
            for(PurchaseOrderItemDto purchaseOrderItemDto : request.getPurchaseOrderItemDtoList()) {
                PurchaseOrderItem purchaseOrderItem = modelMapper.map(purchaseOrderItemDto, PurchaseOrderItem.class);

                purchaseOrderItem.insertPurchase(
                        PurchaseOrder.builder()
                                .purchaseOrderSeq(purchaseOrderSeq)
                                .build()
                );

                purchaseOrderItem.insertItem(
                        Item.builder()
                                .itemSeq(purchaseOrderItemDto.getItemSeq())
                                .build()
                );

                items.add(purchaseOrderItem);
            }
            purchaseOrderItemRepository.saveAll(items);
        }
    }

    public void updatePurchaseOrderItem(PurchaseOrderUpdateRequest request) {
        if(ObjectUtils.isNotEmpty(request)) {
            for(PurchaseOrderItemDto purchaseOrderItem : request.getPurchaseOrderItemDtoList()) {

                // 발주서는 주문서의 품목과 상관없이 발주할 수 있어야함. 그래서 주문서의 품목과는 상관없이 추가할 수 있고,
                // 주문서에 있는 품목일 경우는 수량 등을 변경할 수 있다.
                if(purchaseOrderItem.getPurchaseOrderItemSeq() != null) {
                    PurchaseOrderItem purChaseOrderItemResponse = purchaseOrderItemRepository.findById(purchaseOrderItem.getPurchaseOrderItemSeq())
                            .orElseThrow( () -> new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND));
                    purChaseOrderItemResponse.updateOrderInfo(purchaseOrderItem.getPurchaseOrderItemQuantity(), purchaseOrderItem.getPurchaseOrderItemPrice(), purchaseOrderItem.getPurchaseOrderItemNote());
                } else {
                    PurchaseOrderItem purchaseOrderItemRequest = modelMapper.map(purchaseOrderItem, PurchaseOrderItem.class);

                    purchaseOrderItemRequest.insertPurchase(
                            PurchaseOrder.builder()
                                    .purchaseOrderSeq(request.getPurchaseOrderSeq())
                                    .build()
                    );

                    purchaseOrderItemRequest.insertItem(
                            Item.builder()
                                    .itemSeq(purchaseOrderItem.getItemSeq())
                                    .build()
                    );

                    purchaseOrderItemRepository.save(purchaseOrderItemRequest);
                }
            }
        }
    }

}

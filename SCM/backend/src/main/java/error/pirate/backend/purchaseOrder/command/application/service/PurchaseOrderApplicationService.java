package error.pirate.backend.purchaseOrder.command.application.service;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.client.command.domain.aggregate.repository.ClientRepository;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderCreateRequest;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderItemDto;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderItem;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderStatus;
import error.pirate.backend.purchaseOrder.command.domain.service.PurchaseOrderDomainService;
import error.pirate.backend.purchaseOrder.command.domain.service.PurchaseOrderItemDomainService;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderApplicationService {

    private final PurchaseOrderDomainService purchaseOrderDomainService;

    private final PurchaseOrderItemDomainService purchaseOrderItemDomainService;

    private final SalesOrderDomainService salesOrderDomainService;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final ModelMapper modelMapper;

    private final ItemRepository itemRepository;

    @Transactional
    public void createPurchaseOrder(PurchaseOrderCreateRequest request) {
        //TODO 아영 - 로그인이 완료되면 userSeq 정보 넣기

        PurchaseOrder purchaseOrder = modelMapper.map(request, PurchaseOrder.class);

        User user = userRepository.findById(request.getUserSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));

        Client client = clientRepository.findById(request.getClientSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.CLIENT_NOT_FOUND));

        purchaseOrder.objectInjection(user, client);
        purchaseOrder.changePurchaseOrderTotalQuantity(
                request.getPurchaseOrderItemDtoList().stream().mapToInt(PurchaseOrderItemDto::getPurchaseOrderItemQuantity).sum()
        );
        PurchaseOrder purchaseOrderResponse = purchaseOrderDomainService.createPurchaseOrder(purchaseOrder);

        List<PurchaseOrderItem> items = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(request.getPurchaseOrderItemDtoList())) {
            for (PurchaseOrderItemDto purchaseOrderItemDto : request.getPurchaseOrderItemDtoList()) {
                PurchaseOrderItem purchaseOrderItem = modelMapper.map(purchaseOrderItemDto, PurchaseOrderItem.class);

                purchaseOrderItem.insertPurchase(purchaseOrderResponse);

                Item item = itemRepository.findById(purchaseOrderItemDto.getItemSeq())
                        .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
                purchaseOrderItem.insertItem(item);

                items.add(purchaseOrderItem);
            }
        }
        purchaseOrderItemDomainService.createPurchaseOrderItem(items);
    }

    @Transactional
    public void updatePurchaseOrder(PurchaseOrderUpdateRequest request) {
        purchaseOrderDomainService.updatePurchaseOrder(request);

        if(ObjectUtils.isNotEmpty(request.getPurchaseOrderItemDtoList())) {
            List<PurchaseOrderItem> items = new ArrayList<>();

            for(PurchaseOrderItemDto purchaseOrderItem : request.getPurchaseOrderItemDtoList()) {

                // 있는 품목일 경우는 수량 등을 변경
                if(purchaseOrderItem.getPurchaseOrderItemSeq() != null) {
                    purchaseOrderItemDomainService.updatePurchaseOrderItem(purchaseOrderItem);
                } else {
                    // 없는 품목일 경우 추가
                    PurchaseOrderItem purchaseOrderItemRequest = modelMapper.map(purchaseOrderItem, PurchaseOrderItem.class);

                    PurchaseOrder purchaseOrder = purchaseOrderDomainService.findById(request.getPurchaseOrderSeq());
                    purchaseOrderItemRequest.insertPurchase(purchaseOrder);

                    Item item = itemRepository.findById(purchaseOrderItem.getItemSeq())
                            .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
                    purchaseOrderItemRequest.insertItem(item);

                    items.add(purchaseOrderItemRequest);
                }
            }
            purchaseOrderItemDomainService.createPurchaseOrderItem(items);
        }

    }

    @Transactional
    public void deletePurchaseOrder(Long purchaseOrderSeq) {
        purchaseOrderDomainService.updatePurchaseStatus(purchaseOrderSeq, PurchaseOrderStatus.DELETE);
    }

    @Transactional
    public void updatePurchaseOrderComplete(Long purchaseOrderSeq) {
        purchaseOrderDomainService.updatePurchaseStatus(purchaseOrderSeq, PurchaseOrderStatus.APPROVAL_AFTER);
    }

    @Transactional
    public void updatePurchaseOrderRefusal(Long purchaseOrderSeq) {
        purchaseOrderDomainService.updatePurchaseStatus(purchaseOrderSeq, PurchaseOrderStatus.APPROVAL_REFUSAL);
    }

}

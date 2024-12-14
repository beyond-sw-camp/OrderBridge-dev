package error.pirate.backend.purchase.command.application.service;

import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.item.command.domain.repository.ItemRepository;
import error.pirate.backend.purchase.command.application.dto.PurchaseCreateRequest;
import error.pirate.backend.purchase.command.application.dto.PurchaseItemDto;
import error.pirate.backend.purchase.command.application.dto.PurchaseUpdateRequest;
import error.pirate.backend.purchase.command.domain.aggregate.entity.Purchase;
import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseItem;
import error.pirate.backend.purchase.command.domain.aggregate.entity.PurchaseStatus;
import error.pirate.backend.purchase.command.domain.service.PurchaseDomainService;
import error.pirate.backend.purchase.command.domain.service.PurchaseItemDomainService;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderStatus;
import error.pirate.backend.purchaseOrder.command.domain.service.PurchaseOrderDomainService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import error.pirate.backend.user.command.domain.service.UserDomainService;
import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import error.pirate.backend.warehouse.command.domain.service.WarehouseDomainService;
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
public class PurchaseApplicationService {

    private final PurchaseOrderDomainService purchaseOrderDomainService;

    private final PurchaseDomainService purchaseDomainService;

    private final PurchaseItemDomainService purchaseItemDomainService;

    private final WarehouseDomainService warehouseDomainService;

    private final UserDomainService userDomainService;

    private final ModelMapper modelMapper;

    private final ItemRepository itemRepository;

    @Transactional
    public void createPurchase(PurchaseCreateRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderDomainService.findById(request.getPurchaseOrderSeq());
        if(purchaseOrder.getPurchaseOrderStatus() == PurchaseOrderStatus.APPROVAL_AFTER) {
            Purchase purchase = modelMapper.map(request, Purchase.class);

            User user = userDomainService.findById(request.getUserSeq());
            Warehouse warehouse = warehouseDomainService.findById(request.getWarehouseSeq());
            purchase.objectInjection(user, warehouse, purchaseOrder);

            Purchase purchaseResponse = purchaseDomainService.createPurchase(purchase);

            List<PurchaseItem> items = new ArrayList<>();
            if(ObjectUtils.isNotEmpty(request.getPurchaseItemDtoList())) {
                for(PurchaseItemDto purchaseItemDto : request.getPurchaseItemDtoList()) {
                    PurchaseItem purchaseItem = modelMapper.map(purchaseItemDto, PurchaseItem.class);

                    purchaseItem.insertPurchase(purchaseResponse);

                    Item item = itemRepository.findById(purchaseItemDto.getItemSeq())
                            .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
                    purchaseItem.insertItem(item);

                    items.add(purchaseItem);
                }
            }
            purchaseItemDomainService.createPurchaseItem(items);
        } else {
            throw new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND);
        }
    }

    @Transactional
    public void updatePurchase(PurchaseUpdateRequest request) {
        purchaseDomainService.updatePurchase(request);

        if(ObjectUtils.isNotEmpty(request.getPurchaseItemDtoList())) {
            List<PurchaseItem> items = new ArrayList<>();

            for(PurchaseItemDto purchaseItem : request.getPurchaseItemDtoList()) {

                // 있는 품목일 경우는 수량 등을 변경
                if(purchaseItem.getPurchaseItemSeq() != null) {
                    purchaseItemDomainService.updatePurchaseItem(purchaseItem);
                } else {
                    // 없는 품목일 경우 추가
                    PurchaseItem purchaseItemRequest = modelMapper.map(purchaseItem, PurchaseItem.class);

                    Purchase purchase = purchaseDomainService.findById(request.getPurchaseOrderSeq());
                    purchaseItemRequest.insertPurchase(purchase);

                    Item item = itemRepository.findById(purchaseItem.getItemSeq())
                            .orElseThrow(() -> new CustomException(ErrorCodeType.ITEM_NOT_FOUND));
                    purchaseItemRequest.insertItem(item);

                    items.add(purchaseItemRequest);
                }
            }
            purchaseItemDomainService.createPurchaseItem(items);
        }

    }

    @Transactional
    public void updatePurchaseComplete(Long purchaseSeq) {
        purchaseDomainService.updatePurchaseStatus(purchaseSeq, PurchaseStatus.COMPLETE);
    }

    @Transactional
    public void deletePurchase(Long purchaseSeq) {
        purchaseDomainService.updatePurchaseStatus(purchaseSeq, PurchaseStatus.DELETE);
    }

}

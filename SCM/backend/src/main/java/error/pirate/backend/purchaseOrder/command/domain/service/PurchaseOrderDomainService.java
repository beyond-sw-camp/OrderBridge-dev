package error.pirate.backend.purchaseOrder.command.domain.service;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderCreateRequest;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderItemDto;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrder;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderItem;
import error.pirate.backend.purchaseOrder.command.domain.aggregate.entity.PurchaseOrderStatus;
import error.pirate.backend.purchaseOrder.command.domain.repository.PurchaseOrderItemRepository;
import error.pirate.backend.purchaseOrder.command.domain.repository.PurchaseOrderRepository;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderDomainService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    public void createPurchaseOrder(PurchaseOrderCreateRequest request) {
        PurchaseOrder purchaseOrder = modelMapper.map(request, PurchaseOrder.class);

        User user = Optional.ofNullable(entityManager.find(User.class, request.getUserSeq()))
                .orElseThrow(() -> new CustomException(ErrorCodeType.USER_NOT_FOUND));
        Client client = Optional.ofNullable(entityManager.find(Client.class, request.getClientSeq()))
                .orElseThrow(() -> new CustomException(ErrorCodeType.CLIENT_NOT_FOUND));
        SalesOrder salesOrder = Optional.ofNullable(entityManager.find(SalesOrder.class, request.getSalesOrderSeq()))
                .orElseThrow(() -> new CustomException(ErrorCodeType.SALES_ORDER_NOT_FOUND));

        purchaseOrder.setSeqValue(user, client, salesOrder);

        // 발주서 이름 생성
        int purchaseOrderCount = (int) purchaseOrderRepository.count();
        
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String purchaseOrderName = today.format(formatter) + " - "  + (purchaseOrderCount+1);
        purchaseOrder.changePurchaseOrderName(purchaseOrderName);

        // PurchaseOrder 저장
        PurchaseOrder purchaseOrderResponse = purchaseOrderRepository.save(purchaseOrder);

        // PurchaseOrderItem 저장
        List<PurchaseOrderItem> items = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(request.getPurchaseOrderItemDtoList())) {
            for(PurchaseOrderItemDto purchaseOrderItemDto : request.getPurchaseOrderItemDtoList()) {
                PurchaseOrderItem purchaseOrderItem = modelMapper.map(purchaseOrderItemDto, PurchaseOrderItem.class);
                purchaseOrderItem.insertPurchase(purchaseOrderResponse);

                items.add(purchaseOrderItem);
            }
            purchaseOrderItemRepository.saveAll(items);
        }
    }

    public void updatePurchaseOrder(PurchaseOrderUpdateRequest request) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(request.getPurchaseOrderSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND));
        purchaseOrder.updatePurchaseOrder(request);
    }

    public void deletePurchaseOrder(Long purchaseOrderSeq) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderSeq)
                .orElseThrow(() -> new CustomException(ErrorCodeType.PURCHASE_NOT_FOUND));
        purchaseOrder.changePurchaseOrderStatus(PurchaseOrderStatus.DELETE);
    }


}

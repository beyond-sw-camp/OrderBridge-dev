package error.pirate.backend.salesOrder.command.application.service;

import error.pirate.backend.client.command.domain.aggregate.entity.Client;
import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.quotation.command.domain.aggregate.entity.Quotation;
import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationItem;
import error.pirate.backend.quotation.command.domain.repository.QuotationItemRepository;
import error.pirate.backend.salesOrder.command.application.dto.SalesOrderItemRequest;
import error.pirate.backend.salesOrder.command.application.dto.CreateSalesOrderRequest;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderItemRepository;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderRepository;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.salesOrder.query.dto.SalesOrderItemCheckDTO;
import error.pirate.backend.salesOrder.query.service.SalesOrderQueryService;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesOrderCommandService {

    private final QuotationItemRepository quotationItemRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOrderQueryService salesOrderQueryService;
    private final SalesOrderDomainService salesOrderDomainService;
    private final EntityManager entityManager;
    private final NameGenerator nameGenerator;

    // 주문서 등록
    @Transactional
    public void createSalesOrder(CreateSalesOrderRequest createSalesOrderRequest) {

        // 불러온 견적서가 있으면 견적서 확인 절차
        validateItem(createSalesOrderRequest.getQuotationSeq(), createSalesOrderRequest.getSalesOrderItemList());

        // 주문서 등록
        Quotation quotation = entityManager.getReference(Quotation.class, createSalesOrderRequest.getQuotationSeq());
        Client client = entityManager.getReference(Client.class, createSalesOrderRequest.getClientSeq());
        User user = entityManager.getReference(User.class, 1L);
        String name = nameGenerator.nameGenerator(SalesOrder.class);

        SalesOrder salesOrder = new SalesOrder(quotation, user, client, name,
                createSalesOrderRequest.getSalesOrderOrderDate(),
                createSalesOrderRequest.getSalesOrderDueDate(),
                createSalesOrderRequest.getSalesOrderNote());

        salesOrderRepository.save(salesOrder);

        // 주문서 품목 등록
        for (SalesOrderItemRequest salesOrderItemRequest : createSalesOrderRequest.getSalesOrderItemList()) {
            Item item = entityManager.getReference(Item.class, salesOrderItemRequest.getItemSeq());
            SalesOrderItem salesOrderItem = new SalesOrderItem(salesOrder, item,
                    salesOrderItemRequest.getSalesOrderItemQuantity(), salesOrderItemRequest.getSalesOrderItemPrice(),
                    salesOrderItemRequest.getSalesOrderItemNote());

            salesOrderItemRepository.save(salesOrderItem);
        }

        // 견적서와 주문서의 품목 수량 비교
        salesOrderDomainService.validateItem(createSalesOrderRequest.getQuotationSeq());
    }

    // 견적서 품목과 주문서 품목에 대한 검증
    public void validateItem(Long quotationSeq, List<SalesOrderItemRequest> salesOrderItemRequestList) {
        List<QuotationItem> quotationItemList =
                quotationItemRepository.findByQuotationQuotationSeq(quotationSeq);

        int matchCount = 0;
        for (QuotationItem quotationItem : quotationItemList) {
            for (SalesOrderItemRequest salesOrderItemRequest : salesOrderItemRequestList) {
                if (quotationItem.getItem().getItemSeq().equals(salesOrderItemRequest.getItemSeq())) {
                    matchCount++;
                }
            }
        }

        if (quotationItemList.size() < salesOrderItemRequestList.size()
                || matchCount != salesOrderItemRequestList.size()) {
            throw new CustomException(ErrorCodeType.SALES_ORDER_ITEM_NOT_MATCH);
        }
    }
}

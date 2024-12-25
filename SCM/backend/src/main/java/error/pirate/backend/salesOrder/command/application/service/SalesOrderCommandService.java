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
import error.pirate.backend.salesOrder.command.application.dto.UpdateSalesOrderItemReqest;
import error.pirate.backend.salesOrder.command.application.dto.UpdateSalesOrderRequest;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderCommandRepository;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderItemRepository;
import error.pirate.backend.salesOrder.command.domain.service.SalesOrderDomainService;
import error.pirate.backend.salesOrder.query.dto.SalesOrderItemDTO;
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
    private final SalesOrderCommandRepository salesOrderCommandRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOrderDomainService salesOrderDomainService;
    private final EntityManager entityManager;
    private final NameGenerator nameGenerator;

    // 주문서 등록
    @Transactional
    public void createSalesOrder(CreateSalesOrderRequest createSalesOrderRequest) {

        // 불러온 견적서가 있으면 견적서 확인 절차
        validateItem(createSalesOrderRequest.getQuotationSeq(), createSalesOrderRequest.getSalesOrderItemList());

        // 엔티티 요구 변수 작성
        Quotation quotation = entityManager.getReference(Quotation.class, createSalesOrderRequest.getQuotationSeq());
        Client client = entityManager.getReference(Client.class, createSalesOrderRequest.getClientSeq());
        User user = entityManager.getReference(User.class, 1L);
        String name = nameGenerator.nameGenerator(SalesOrder.class);

        // 주문서 합계 계산
        int salesOrderExtendedPrice = 0;
        int salesOrderTotalQuantity = 0;

        for (SalesOrderItemRequest salesOrderItemRequest : createSalesOrderRequest.getSalesOrderItemList()) {
            salesOrderExtendedPrice +=
                    salesOrderItemRequest.getSalesOrderItemQuantity() * salesOrderItemRequest.getSalesOrderItemPrice();
            salesOrderTotalQuantity +=
                    salesOrderItemRequest.getSalesOrderItemQuantity();
        }

        // 주문서 등록
        SalesOrder salesOrder = new SalesOrder(quotation, user, client, name,
                createSalesOrderRequest.getSalesOrderOrderDate(),
                createSalesOrderRequest.getSalesOrderDueDate(),
                createSalesOrderRequest.getSalesOrderNote(),
                salesOrderExtendedPrice, salesOrderTotalQuantity);

        salesOrderCommandRepository.save(salesOrder);

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

    // 주문서 수정
    @Transactional
    public void updateSalesOrder(Long salesOrderSeq, UpdateSalesOrderRequest request) {
        SalesOrder salesOrder = salesOrderCommandRepository.findById(salesOrderSeq).orElseThrow();

        // 엔티티 요구 변수 작성
        Client client = request.getClientSeq() != null ?
                entityManager.getReference(Client.class, request.getClientSeq()) : null;
        User user = entityManager.getReference(User.class, 1L);

        // 기존 주문서 품목 삭제
        ArrayList<SalesOrderItem> salesOrderItemList = salesOrderItemRepository.findBySalesOrderSalesOrderSeq(salesOrderSeq);

        for (SalesOrderItem salesOrderItem : salesOrderItemList) {
            salesOrderItemRepository.delete(salesOrderItem);
        }

        // 합계 계산용 변수
        int salesOrderExtendedPrice = 0;
        int salesOrderTotalQuantity = 0;

        // 주문서 품목 등록
        for (UpdateSalesOrderItemReqest updateSalesOrderItemReqest : request.getSalesOrderItemReqestList()) {
            // 주문서 합계 계산
            salesOrderExtendedPrice += updateSalesOrderItemReqest.getSalesOrderItemPrice()
                    * updateSalesOrderItemReqest.getSalesOrderItemQuantity();
            salesOrderTotalQuantity += updateSalesOrderItemReqest.getSalesOrderItemQuantity();

            SalesOrderItem salesOrderItem = new SalesOrderItem(
                    entityManager.getReference(SalesOrder.class, salesOrderSeq),
                    entityManager.getReference(Item.class, updateSalesOrderItemReqest.getItemSeq()),
                    updateSalesOrderItemReqest.getSalesOrderItemQuantity(),
                    updateSalesOrderItemReqest.getSalesOrderItemPrice(),
                    updateSalesOrderItemReqest.getSalesOrderItemNote());
            salesOrderItemRepository.save(salesOrderItem);
        }

        // 주문서 변경 사항 적용
        salesOrder.updateSalesOrder(
                request.getSalesOrderOrderDate(), request.getSalesOrderDueDate(), client, user,
                request.getSalesOrderNote(), salesOrderExtendedPrice, salesOrderTotalQuantity);

        // 견적서와 주문서의 품목 수량 비교
        salesOrderDomainService.validateItem(salesOrder.getQuotation().getQuotationSeq());
    }

    // 견적서 삭제
    @Transactional
    public void deleteSalesOrder(Long salesOrderSeq) {

        SalesOrder salesOrder = salesOrderCommandRepository.findById(salesOrderSeq).orElseThrow();

        salesOrder.delete();
    }
}

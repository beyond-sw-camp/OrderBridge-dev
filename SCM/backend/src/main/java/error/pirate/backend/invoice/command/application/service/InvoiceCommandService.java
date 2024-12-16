package error.pirate.backend.invoice.command.application.service;

import error.pirate.backend.common.NameGenerator;
import error.pirate.backend.exception.CustomException;
import error.pirate.backend.exception.ErrorCodeType;
import error.pirate.backend.invoice.command.application.controller.UpdateInvoiceRequest;
import error.pirate.backend.invoice.command.application.dto.CreateInvoiceItemRequest;
import error.pirate.backend.invoice.command.application.dto.CreateInvoiceRequest;
import error.pirate.backend.invoice.command.application.dto.UpdateInvoiceItemRequest;
import error.pirate.backend.invoice.command.domain.aggregate.entity.Invoice;
import error.pirate.backend.invoice.command.domain.aggregate.entity.InvoiceItem;
import error.pirate.backend.invoice.command.domain.repository.InvoiceItemRepository;
import error.pirate.backend.invoice.command.domain.repository.InvoiceRepository;
import error.pirate.backend.invoice.command.domain.service.InvoiceDomainService;
import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrder;
import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderItem;
import error.pirate.backend.salesOrder.command.domain.repository.SalesOrderItemRepository;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceCommandService {

    private final InvoiceDomainService invoiceDomainService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final EntityManager entityManager;
    private final NameGenerator nameGenerator;

    @Transactional
    public void createInvoice(CreateInvoiceRequest createInvoiceRequest) {

        // 주문서 품목과 거래 명세서 품목에 대한 검증
        validateItem(createInvoiceRequest.getSalesOrderSeq(), createInvoiceRequest.getCreateInvoiceItemRequestList());

        // 거래 명세서 등록
        SalesOrder salesOrder = entityManager.getReference(SalesOrder.class, createInvoiceRequest.getSalesOrderSeq());
        User user = entityManager.getReference(User.class, 1L);
        String name = nameGenerator.nameGenerator(Invoice.class);

        int invoiceExtendedPrice = 0;
        int invoiceTotalQuantity = 0;

        for (CreateInvoiceItemRequest createInvoiceItemRequest : createInvoiceRequest.getCreateInvoiceItemRequestList()) {
            invoiceExtendedPrice += createInvoiceItemRequest.getInvoiceItemQuantity()
                    * createInvoiceItemRequest.getInvoiceItemPrice();
            invoiceTotalQuantity += createInvoiceItemRequest.getInvoiceItemQuantity();
        }

        Invoice invoice = new Invoice(salesOrder, user, name, createInvoiceRequest.getInvoiceSaleDate(),
                invoiceExtendedPrice, invoiceTotalQuantity, createInvoiceRequest.getInvoiceNote());

        invoiceRepository.save(invoice);

        // 거래 명세서 품목 등록
        for (CreateInvoiceItemRequest createInvoiceItemRequest : createInvoiceRequest.getCreateInvoiceItemRequestList()) {
            Item item = entityManager.getReference(Item.class, createInvoiceItemRequest.getItemSeq());
            InvoiceItem invoiceItem = new InvoiceItem(invoice, item,
                    createInvoiceItemRequest.getInvoiceItemQuantity(), createInvoiceItemRequest.getInvoiceItemPrice(),
                    createInvoiceItemRequest.getInvoiceItemNote());

            invoiceItemRepository.save(invoiceItem);
        }

        // 주문서와 거래 명세서의 품목 수량 비교
        invoiceDomainService.validateItem(createInvoiceRequest.getSalesOrderSeq());
    }

    // 주문서 품목과 거래 명세서 품목에 대한 검증
    private void validateItem(Long salesOrderSeq, List<CreateInvoiceItemRequest> createInvoiceItemRequestList) {
        List<SalesOrderItem> salesOrderItemList = salesOrderItemRepository.findBySalesOrderSalesOrderSeq(salesOrderSeq);

        int matchCount = 0;
        for (SalesOrderItem salesOrderItem : salesOrderItemList) {
            for (CreateInvoiceItemRequest createInvoiceItemRequest : createInvoiceItemRequestList) {
                if (salesOrderItem.getItem().getItemSeq().equals(createInvoiceItemRequest.getItemSeq())) {
                    matchCount++;
                }
            }
        }

        if (salesOrderItemList.size() < createInvoiceItemRequestList.size()
                || matchCount != createInvoiceItemRequestList.size()) {
            throw new CustomException(ErrorCodeType.INVOICE_ITEM_NOT_MATCH);
        }
    }

    // 거래 명세서 수정
    @Transactional
    public void updateInvoice(Long invoiceSeq, UpdateInvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(invoiceSeq).orElseThrow();

        // 엔티티 요구 변수 작성
        User user = entityManager.getReference(User.class, 1L);

        // 기존 거래 명세서 품목 삭제
        ArrayList<InvoiceItem> invoiceItemList = invoiceItemRepository.findByInvoiceInvoiceSeq(invoiceSeq);

        for (InvoiceItem invoiceItem : invoiceItemList) {
            invoiceItemRepository.delete(invoiceItem);
        }

        // 합계 계산용 변수
        int invoiceExtendedPrice = 0;
        int invoiceTotalQuantity = 0;

        // 거래 명세서 품목 등록
        for (UpdateInvoiceItemRequest updateInvoiceItemRequest : request.getUpdateInvoiceItemRequestList()) {
            invoiceExtendedPrice += updateInvoiceItemRequest.getInvoiceItemPrice()
                    * updateInvoiceItemRequest.getInvoiceItemQuantity();
            invoiceTotalQuantity += updateInvoiceItemRequest.getInvoiceItemQuantity();

            InvoiceItem invoiceItem = new InvoiceItem(
                    entityManager.getReference(Invoice.class, invoiceSeq),
                    entityManager.getReference(Item.class, updateInvoiceItemRequest.getItemSeq()),
                    updateInvoiceItemRequest.getInvoiceItemQuantity(),
                    updateInvoiceItemRequest.getInvoiceItemPrice(),
                    updateInvoiceItemRequest.getInvoiceItemNote());
            invoiceItemRepository.save(invoiceItem);
        }

        // 거래 명세서 변경 사항 적용
        invoice.updateInvoice(
                user, request.getInvoiceSaleDate(), request.getInvoiceNote(),
                invoiceExtendedPrice, invoiceTotalQuantity);

        invoiceDomainService.validateItem(invoice.getSalesOrder().getSalesOrderSeq());
    }
}

package error.pirate.backend.invoice.command.application.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateInvoiceRequest {
    private Long salesOrderSeq;
    private LocalDateTime invoiceSaleDate;
    private String invoiceNote;
    private List<CreateInvoiceItemRequest> createInvoiceItemRequestList;
}

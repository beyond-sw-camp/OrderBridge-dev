package error.pirate.backend.invoice.command.application.dto;

import lombok.Getter;

@Getter
public class CreateInvoiceItemRequest {
    private Long itemSeq;
    private Integer invoiceItemQuantity;
    private Integer invoiceItemPrice;
    private String invoiceItemNote;
}

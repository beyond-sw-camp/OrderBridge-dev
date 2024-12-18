package error.pirate.backend.invoice.command.application.dto;

import lombok.Getter;

@Getter
public class UpdateInvoiceItemRequest {
    private Long itemSeq;
    private Integer invoiceItemQuantity;
    private Integer invoiceItemPrice;
    private String invoiceItemNote;
}

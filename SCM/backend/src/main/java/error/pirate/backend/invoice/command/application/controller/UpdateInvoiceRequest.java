package error.pirate.backend.invoice.command.application.controller;

import error.pirate.backend.invoice.command.application.dto.UpdateInvoiceItemRequest;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UpdateInvoiceRequest {
    private LocalDateTime invoiceSaleDate;
    private String invoiceNote;
    private List<UpdateInvoiceItemRequest> updateInvoiceItemRequestList;
}

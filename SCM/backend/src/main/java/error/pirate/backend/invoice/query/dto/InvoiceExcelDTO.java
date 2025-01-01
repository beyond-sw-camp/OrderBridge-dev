package error.pirate.backend.invoice.query.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class InvoiceExcelDTO {
    private LocalDateTime invoiceRegDate;
    private LocalDateTime invoiceModDate;
    private String invoiceName;
    private LocalDate invoiceSaleDate;
    private Integer invoiceTotalQuantity;
    private Integer invoiceExtendedPrice;
    private String invoiceNote;
}

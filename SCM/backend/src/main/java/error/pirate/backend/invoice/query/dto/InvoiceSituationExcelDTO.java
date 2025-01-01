package error.pirate.backend.invoice.query.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvoiceSituationExcelDTO {
    private LocalDate invoiceSaleDate;
    private String invoiceName;
    private Integer invoiceTotalQuantity;
    private Integer invoiceExtendedPrice;
    private String clientName;
    private String invoiceNote;
}

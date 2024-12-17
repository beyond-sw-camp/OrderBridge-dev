package error.pirate.backend.invoice.query.dto;

import lombok.Getter;

@Getter
public class InvoiceItemCheckDTO {
    private Long item;
    private Integer remainingQuantity;
}

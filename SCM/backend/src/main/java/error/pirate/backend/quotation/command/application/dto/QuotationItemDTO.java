package error.pirate.backend.quotation.command.application.dto;

import lombok.Getter;

@Getter
public class QuotationItemDTO {
    private Long itemSeq;
    private Integer quotationItemQuantity;
    private Integer quotationItemPrice;
    private String quotationItemNote;
}

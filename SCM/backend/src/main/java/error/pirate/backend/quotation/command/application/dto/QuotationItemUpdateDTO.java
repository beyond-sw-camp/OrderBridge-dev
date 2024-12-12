package error.pirate.backend.quotation.command.application.dto;

import lombok.Getter;

@Getter
public class QuotationItemUpdateDTO {
//    private Long quotationItemSeq;
    private Long itemSeq;
    private Integer quotationItemQuantity;
    private Integer quotationItemPrice;
    private String quotationItemNote;
}

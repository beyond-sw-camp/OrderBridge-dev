package error.pirate.backend.quotation.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QuotationItemDTO {
    private Long quotationItemSeq;
    private Long itemSeq;
    private String itemImageUrl;
    private String itemDivision;
    private String itemName;
    private Integer quotationItemQuantity;
    private Integer quotationItemPrice;
    private String quotationItemNote;
}

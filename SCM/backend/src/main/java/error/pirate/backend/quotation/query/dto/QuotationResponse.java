package error.pirate.backend.quotation.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class QuotationResponse {
    private Long quotationSeq;
    private String quotationName;
    private String clientName;
    private Integer quotationExtendedPrice;
    private Integer quotationTotalQuantity;
    private String userName;
    private LocalDateTime quotationQuotationDate;
    private LocalDateTime quotationEffectiveDate;
    private String quotationNote;
    @Setter
    private List<QuotationItemDTO> quotationItem;
}

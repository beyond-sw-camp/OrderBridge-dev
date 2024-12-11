package error.pirate.backend.quotation.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class QuotationSituationDTO {
    private Long quotationSeq;
    private LocalDate quotationQuotationDate;
    private String quotationName;
    private Integer quotationTotalQuantity;
    private Integer quotationExtendedPrice;
    private String clientName;
    private String quotationNote;
}

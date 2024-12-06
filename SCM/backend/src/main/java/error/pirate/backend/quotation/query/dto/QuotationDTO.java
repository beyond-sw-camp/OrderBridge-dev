package error.pirate.backend.quotation.query.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class QuotationDTO {
    private Long quotationSeq;
    private String quotationName;
    private String clientName;
    private Integer quotationExtendedPrice;
    private Integer quotationTotalQuantity;
    private String userName;
    private LocalDateTime quotationQuotationDate;
    private LocalDateTime quotationEffectiveDate;
    private String quotationNote;
}

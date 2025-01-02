package error.pirate.backend.quotation.query.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class QuotationSituationExcelDTO {
    private LocalDate quotationQuotationDate;
    private String quotationName;
    private Integer quotationTotalQuantity;
    private Integer quotationExtendedPrice;
    private String clientName;
    private String quotationNote;
}

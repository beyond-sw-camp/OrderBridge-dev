package error.pirate.backend.quotation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class QuotationSituationResponse {
    private LocalDate quotationQuotationDate;
    private String quotationName;
    private Integer quotationTotalQuantity;
    private Integer quotationExtendedPrice;
    private String clientName;
    private String quotationNote;

    private String quotationQuotationMonth;
    private Integer quotationMonthPrice;
    private Integer quotationMonthQuantity;
}

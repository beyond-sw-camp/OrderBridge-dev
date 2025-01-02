package error.pirate.backend.quotation.query.dto;

import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationStatus;
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

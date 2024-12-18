package error.pirate.backend.quotation.query.dto;

import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class QuotationExcelDTO {
    private LocalDateTime quotationRegDate;
    private LocalDateTime quotationModDate;
    private String quotationName;
    private QuotationStatus quotationStatus;
    private LocalDate quotationQuotationDate;
    private LocalDate quotaitonEffectiveDate;
    private Integer quotationTotalQuantity;
    private Integer quotationExtendedPrice;
    private String quotationNote;
}

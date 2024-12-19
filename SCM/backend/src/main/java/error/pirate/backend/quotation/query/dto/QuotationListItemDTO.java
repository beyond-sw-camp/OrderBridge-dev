package error.pirate.backend.quotation.query.dto;

import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class QuotationListItemDTO {
    private Long quotationSeq;
    private String quotationName;
    private String itemName;
    private String clientName;
    private LocalDate quotationQuotationDate;
    private QuotationStatus quotationStatus;
}

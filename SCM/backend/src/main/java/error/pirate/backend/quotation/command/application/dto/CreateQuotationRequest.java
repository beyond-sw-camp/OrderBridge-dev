package error.pirate.backend.quotation.command.application.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateQuotationRequest {
    private LocalDateTime quotationQuotationDate;
    private Long clientSeq;
    private String quotationNote;
    private List<QuotationItemDTO> quotationItem;
}

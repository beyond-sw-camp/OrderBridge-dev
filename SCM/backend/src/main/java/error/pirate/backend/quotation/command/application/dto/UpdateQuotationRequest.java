package error.pirate.backend.quotation.command.application.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UpdateQuotationRequest {
    private LocalDateTime quotationQuotationDate;
    private Long clientSeq;
    private Long userSeq;
    private String quotationNote;
//    private List<Long> quotationItemSeqList;
    private List<QuotationItemUpdateDTO> quotationItemUpdateList;
}

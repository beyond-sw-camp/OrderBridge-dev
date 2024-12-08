package error.pirate.backend.quotation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuotationSituationResponse {
    private List<QuotationSituationDTO> quotationSituationList;
}

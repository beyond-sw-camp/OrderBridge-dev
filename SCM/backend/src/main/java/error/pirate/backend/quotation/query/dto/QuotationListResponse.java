package error.pirate.backend.quotation.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuotationListResponse {
    private List<QuotationListItemDTO> quotation;
    private int currentPage;
    private int totalPages;
    private int totalQuotation;
}

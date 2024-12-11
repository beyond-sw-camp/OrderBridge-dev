package error.pirate.backend.quotation.command.domain.repository;

import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationItem;

import java.util.ArrayList;

public interface QuotationItemRepository {
    QuotationItem save(QuotationItem quotationItem);

    ArrayList<QuotationItem> findByQuotationQuotationSeq(Long quotationSeq);

    void delete(QuotationItem quotationItem);
}

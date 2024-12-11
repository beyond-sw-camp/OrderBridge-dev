package error.pirate.backend.quotation.command.domain.repository;

import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationItem;

public interface QuotationItemRepository {
    QuotationItem save(QuotationItem quotationItem);
}

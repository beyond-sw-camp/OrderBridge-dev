package error.pirate.backend.quotation.command.domain.repository;

import error.pirate.backend.quotation.command.domain.aggregate.entity.Quotation;

public interface QuotationRepository {
    Quotation save(Quotation quotation);
}

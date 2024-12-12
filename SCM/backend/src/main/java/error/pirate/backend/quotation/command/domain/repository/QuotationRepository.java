package error.pirate.backend.quotation.command.domain.repository;

import error.pirate.backend.quotation.command.domain.aggregate.entity.Quotation;

import java.util.Optional;

public interface QuotationRepository {
    Quotation save(Quotation quotation);

    Optional<Quotation> findById(Long quotationSeq);
}

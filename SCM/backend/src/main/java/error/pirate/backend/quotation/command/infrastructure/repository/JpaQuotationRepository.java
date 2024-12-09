package error.pirate.backend.quotation.command.infrastructure.repository;

import error.pirate.backend.quotation.command.domain.repository.QuotationRepository;
import error.pirate.backend.quotation.command.domain.aggregate.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuotationRepository extends QuotationRepository, JpaRepository<Quotation, Long> {
}

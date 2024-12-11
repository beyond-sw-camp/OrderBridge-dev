package error.pirate.backend.quotation.command.infrastructure.repository;

import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationItem;
import error.pirate.backend.quotation.command.domain.repository.QuotationItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuotationItemRepository extends QuotationItemRepository, JpaRepository<QuotationItem, Long> {
}

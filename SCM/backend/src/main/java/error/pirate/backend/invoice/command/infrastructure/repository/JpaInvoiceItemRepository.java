package error.pirate.backend.invoice.command.infrastructure.repository;

import error.pirate.backend.invoice.command.domain.aggregate.entity.InvoiceItem;
import error.pirate.backend.invoice.command.domain.repository.InvoiceItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInvoiceItemRepository extends InvoiceItemRepository, JpaRepository<InvoiceItem, Long> {
}

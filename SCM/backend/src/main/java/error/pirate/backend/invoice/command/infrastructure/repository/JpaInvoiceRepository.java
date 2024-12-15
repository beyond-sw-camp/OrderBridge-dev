package error.pirate.backend.invoice.command.infrastructure.repository;

import error.pirate.backend.invoice.command.domain.aggregate.entity.Invoice;
import error.pirate.backend.invoice.command.domain.repository.InvoiceRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInvoiceRepository extends InvoiceRepository, JpaRepository<Invoice, Long> {
}

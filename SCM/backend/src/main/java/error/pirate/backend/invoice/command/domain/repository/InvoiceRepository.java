package error.pirate.backend.invoice.command.domain.repository;

import error.pirate.backend.invoice.command.domain.aggregate.entity.Invoice;

import java.util.Optional;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);

    Optional<Invoice> findById(Long invoiceSeq);
}

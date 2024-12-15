package error.pirate.backend.invoice.command.domain.repository;

import error.pirate.backend.invoice.command.domain.aggregate.entity.Invoice;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
}

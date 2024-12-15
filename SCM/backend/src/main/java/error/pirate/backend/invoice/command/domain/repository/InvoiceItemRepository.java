package error.pirate.backend.invoice.command.domain.repository;

import error.pirate.backend.invoice.command.domain.aggregate.entity.InvoiceItem;

public interface InvoiceItemRepository {
    InvoiceItem save(InvoiceItem invoiceItem);
}

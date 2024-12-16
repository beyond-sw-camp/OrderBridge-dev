package error.pirate.backend.invoice.command.domain.repository;

import error.pirate.backend.invoice.command.domain.aggregate.entity.InvoiceItem;

import java.util.ArrayList;

public interface InvoiceItemRepository {
    InvoiceItem save(InvoiceItem invoiceItem);

    ArrayList<InvoiceItem> findByInvoiceInvoiceSeq(Long invoiceSeq);

    void delete(InvoiceItem invoiceItem);
}

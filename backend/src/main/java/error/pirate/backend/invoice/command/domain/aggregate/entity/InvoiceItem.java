package error.pirate.backend.invoice.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_invoice_item") // 거래 명세서 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvoiceItem {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long invoiceItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceSeq")
    private Invoice invoice; // 거래 명세서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private int invoiceItemQuantity; // 거래 명세서 수량

    private int invoiceItemPrice; // 거래 명세서 단가

    private String invoiceItemNote; // 거래 명세서 품목 비고
}

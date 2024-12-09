package error.pirate.backend.quotation.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_quotation_item") // 견적서 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuotationItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quotationItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "quotationSeq")
    private Quotation quotation; // 견적서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private int quotationItemQuantity; // 견적서 품목 수량

    private int quotationItemPrice; // 견적서 품목 단가

    private String quotationItemNote; // 견적서 품목 비고

    public QuotationItem(
            Quotation quotation, Item item, int quotationItemQuantity,
            int quotationItemPrice, String quotationItemNote) {

        this.quotation = quotation;
        this.item = item;
        this.quotationItemQuantity = quotationItemQuantity;
        this.quotationItemPrice = quotationItemPrice;
        this.quotationItemNote = quotationItemNote;
    }
}

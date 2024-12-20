package error.pirate.backend.purchase.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_purchase_item") // 구매서 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseSeq")
    private Purchase purchase; // 구매서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private int purchaseItemQuantity; // 입고 수량

    private int purchaseItemPrice; // 구매 품목 단가

    private LocalDateTime purchaseItemReceiptDate; // 구매 품목 입고일

    @Enumerated(EnumType.STRING)
    private PurchaseItemStatus purchaseItemStatus; // 구매 품목 상태

    private int purchaseOrderExtendedPrice;

    private String purchaseItemNote; // 구매 품목 비고

    public int getPurchaseOrderExtendedPrice() {
        return purchaseItemQuantity * purchaseItemPrice;
    }

    public void insertPurchase(Purchase purchaseResponse) {
        this.purchase = purchaseResponse;
    }

    public void insertItem(Item item) {
        this.item = item;
    }

    public void updateOrderInfo(int purchaseItemQuantity, int purchaseItemPrice, String purchaseItemNote, LocalDateTime purchaseItemReceiptDate) {
        this.purchaseItemQuantity = purchaseItemQuantity;
        this.purchaseItemPrice = purchaseItemPrice;
        this.purchaseItemNote = purchaseItemNote;
        this.purchaseItemReceiptDate = purchaseItemReceiptDate;
    }

}

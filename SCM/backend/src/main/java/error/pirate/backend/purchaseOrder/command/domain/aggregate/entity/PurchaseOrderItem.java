package error.pirate.backend.purchaseOrder.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_purchase_order_item") // 발주서 품목
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseOrderSeq")
    private PurchaseOrder purchaseOrder; // 발주서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private int purchaseOrderItemQuantity; // 발주서 품목 수량

    private int purchaseOrderItemPrice; // 발주서 품목 단가

    private int purchaseOrderItemExtendedPrice;

    private String purchaseOrderItemNote; // 발주서 품목 비고

    public void insertPurchase(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public void insertItem(Item item) {
        this.item = item;
    }

    public void updateOrderInfo(int purchaseOrderItemQuantity, int purchaseOrderItemPrice, String purchaseOrderItemNote) {
        this.purchaseOrderItemQuantity = purchaseOrderItemQuantity;
        this.purchaseOrderItemPrice = purchaseOrderItemPrice;
        this.purchaseOrderItemNote = purchaseOrderItemNote;
    }

}

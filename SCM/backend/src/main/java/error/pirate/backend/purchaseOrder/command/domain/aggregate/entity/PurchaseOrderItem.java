package error.pirate.backend.purchaseOrder.command.domain.aggregate.entity;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import error.pirate.backend.purchase.command.domain.aggregate.entity.Purchase;
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
    private Purchase purchase; // 발주서

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private int purchaseOrderItemQuantity; // 발주서 품목 수량

    private int purchaseOrderItemPrice; // 발주서 품목 단가

    private String purchaseOrderItemNote; // 발주서 품목 비고
}

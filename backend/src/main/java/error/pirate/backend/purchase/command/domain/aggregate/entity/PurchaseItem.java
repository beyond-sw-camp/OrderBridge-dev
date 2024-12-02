package error.pirate.backend.purchase.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_purchase_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseItem {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseItemSeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseSeq")
    private Purchase purchase;

    private int purchaseItemQuantity; // 입고 수량

    private int purchaseItemPrice; // 구매 단가

    private int purchaseItemSettlementQuantity; // 정산 수량

    private LocalDateTime purchaseItemReceiptDate;

    private PurchaseItemStatus purchaseItemStatus;

    private String purchaseItemNote;
}

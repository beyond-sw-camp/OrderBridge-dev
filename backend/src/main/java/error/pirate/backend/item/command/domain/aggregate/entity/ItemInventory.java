package error.pirate.backend.item.command.domain.aggregate.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_item_inventory") // 품목 재고
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInventory {
    @Id @GeneratedValue
    private Long itemInventorySeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private int itemInventoryQuantityReceived; // 품목 재고 입고 수량

    private int itemInventoryRemainAmount; // 품목 재고 잔량

    private LocalDateTime itemInventoryReceiptDate; // 품목 재고 입고일

    private LocalDateTime itemInventoryExpirationDate; // 품목 재고 유통기한

    private String itemInventoryNote; // 품목 재고 비고
}

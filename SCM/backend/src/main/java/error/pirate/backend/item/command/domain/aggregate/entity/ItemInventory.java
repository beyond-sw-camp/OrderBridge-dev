package error.pirate.backend.item.command.domain.aggregate.entity;

import error.pirate.backend.warehouse.command.domain.aggregate.entity.Warehouse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_item_inventory") // 품목 재고
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInventory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemInventorySeq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "itemSeq")
    private Item item; // 품목

    private Integer itemInventoryQuantityReceived; // 품목 재고 입고 수량

    private Integer itemInventoryRemainAmount; // 품목 재고 잔량

    @CreatedDate
    private LocalDateTime itemInventoryReceiptDate; // 품목 재고 입고일

    private LocalDateTime itemInventoryExpirationDate; // 품목 재고 유통기한

    public static ItemInventory createItemInventory(Item item, Integer itemInventoryQuantityReceived,
                                             Integer itemInventoryRemainAmount) {
        ItemInventory itemInventory = new ItemInventory(item, itemInventoryQuantityReceived, itemInventoryRemainAmount);
        itemInventory.specifyItem(item);

        return itemInventory;
    }

    protected ItemInventory(Item item, Integer itemInventoryQuantityReceived, Integer itemInventoryRemainAmount) {
        this.itemInventoryQuantityReceived = itemInventoryQuantityReceived;
        this.itemInventoryRemainAmount = itemInventoryRemainAmount;
        this.itemInventoryReceiptDate = LocalDateTime.now();
        this.itemInventoryExpirationDate = LocalDateTime.now().plusDays(item.getItemExpirationHour());
    }

    private void specifyItem(Item item) {
        this.item = item;
    }
}

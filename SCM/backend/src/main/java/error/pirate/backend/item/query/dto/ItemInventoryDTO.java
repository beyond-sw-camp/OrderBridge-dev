package error.pirate.backend.item.query.dto;

import error.pirate.backend.item.command.domain.aggregate.entity.Item;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemInventoryDTO {
    private Long itemInventorySeq;
    private Long itemSeq;
    private LocalDateTime itemInventoryExpirationDate;
    private Integer itemInventoryQuantityReceived;
    private LocalDateTime itemInventoryReceiptDate;
    private Integer itemInventoryRemainAmount;
    private String itemInventoryNote;

    @Builder
    public static class createPurchaseItem {
        Item item;
        Integer itemInventoryQuantityReceived;
        Integer itemInventoryRemainAmount;
        LocalDateTime itemInventoryReceiptDate;
        LocalDateTime itemInventoryExpirationDate;
    }

}

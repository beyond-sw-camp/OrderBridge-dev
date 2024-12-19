package error.pirate.backend.item.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemInventoryDTO {
    private Long itemInventorySeq;
    private LocalDateTime itemInventoryExpirationDate;
    private Integer itemInventoryQuantityReceived;
    private LocalDateTime itemInventoryReceiptDate;
    private Integer itemInventoryRemainAmount;
    private String itemInventoryNote;
}

package error.pirate.backend.item.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {

    private Long itemSeq;
    private String itemName;
    private String itemDivision;
    private Integer itemExpirationHour;
    private String itemImageUrl;
    private Integer itemPrice;
    private LocalDateTime itemRegDate;
    private LocalDateTime itemModDate;
}

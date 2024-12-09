package error.pirate.backend.item.command.application.dto;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO {
        private Long userSeq;                // 사용자 ID
        private Long itemUnitSeq;            // 품목 단위 ID
        private String itemName;             // 품목 이름
        private ItemDivision itemDivision;         // 품목 구분
        private Integer itemExpirationHour;  // 유통 기한 (시간)
        private String itemImageUrl;         // 이미지 URL
        private Integer itemPrice;           // 품목 가격
    }



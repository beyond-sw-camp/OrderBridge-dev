package error.pirate.backend.item.command.application.dto;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateRequest {
    private Long userSeq;                // 사용자 ID
    private Long itemUnitSeq;            // 품목 단위 ID
    private String itemName;             // 품목 이름
    private ItemDivision itemDivision;   // 품목 구분
    private Integer itemExpirationHour;  // 유통 기한 (시간)
    private String itemImageUrl;         // 이미지 URL
    private Integer itemPrice;           // 품목 가격
    private String itemNote;             // 품목 비고
    private Long warehouseSeq;           //창고

    private List<BomItemDTO> bomItemList = new ArrayList<>(); // Bom 품목
}

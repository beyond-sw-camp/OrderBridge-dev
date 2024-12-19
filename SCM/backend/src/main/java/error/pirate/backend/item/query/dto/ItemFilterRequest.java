package error.pirate.backend.item.query.dto;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemFilterRequest {
    private String itemName;          // 품목명
    private List<ItemDivision> itemDivisions; //품목 구분
    private Integer minExpirationHour; // 최소 유통기한
    private Integer maxExpirationHour; // 최대 유통기한
    private int page; // 현재 페이지
    private int size; // 페이지당 데이터 수

    // 생성자
    public ItemFilterRequest(int page, int size, String itemName, List<ItemDivision> itemDivisions, Integer minExpirationHour, Integer maxExpirationHour) {
        this.page = page;
        this.size = size;
        this.itemName = itemName;
        this.itemDivisions = itemDivisions;
        this.minExpirationHour = minExpirationHour;
        this.maxExpirationHour = maxExpirationHour;
    }

}

package error.pirate.backend.item.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFilterDTO {
    private String itemName;          // 품목명
    private String itemDivision;      // 품목 구분
    private Integer itemExpirationHour; // 유통기한
    private String sortBy;            // 정렬 필드
    private String sortDirection;     // 정렬 방향
    private int page = 1;             // 페이지 번호
    private int size = 10;            // 페이지 크기
}

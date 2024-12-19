package error.pirate.backend.item.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemUnitResponse {
    private Long itemUnitSeq; // 품목 단위 ID
    private String itemUnitTitle; // 품목 단위명
}


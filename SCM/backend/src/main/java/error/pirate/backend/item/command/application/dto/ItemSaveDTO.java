package error.pirate.backend.item.command.application.dto;

import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
import error.pirate.backend.item.command.domain.aggregate.entity.ItemUnit;
import error.pirate.backend.user.command.domain.aggregate.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSaveDTO {

    private Long itemSeq;

    private User user; // 품목 등록자

    private ItemUnit itemUnit; // 품목 단위

    private String itemName; // 품목명

    private ItemDivision itemDivision; // 품목 구분

    private LocalDateTime itemRegDate; // 품목 등록일

    private LocalDateTime itemModDate; // 품목 수정일

    private int itemExpirationHour; // 품목 유통기한(시간)

    private String itemImageUrl; // 품목 이미지 주소

    private Integer itemPrice; // 품목 단가
}

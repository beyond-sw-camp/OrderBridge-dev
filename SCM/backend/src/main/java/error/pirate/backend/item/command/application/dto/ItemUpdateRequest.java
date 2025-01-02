    package error.pirate.backend.item.command.application.dto;

    import error.pirate.backend.item.command.domain.aggregate.entity.ItemDivision;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.ArrayList;
    import java.util.List;

    @Data
    public class ItemUpdateRequest {

        private Long itemUnitSeq;            // 품목 단위 ID
        private Long warehouseSeq;
        private String itemName;             // 품목 이름
        private ItemDivision itemDivision;   // 품목 구분
        private Integer itemExpirationHour;  // 유통 기한 (시간)
        private String itemImageUrl;         // 이미지 URL
        private Integer itemPrice;           // 품목 가격
        private String itemNote;             // 품목 비고

        private List<BomItemDTO> bomItemList = new ArrayList<>(); // bom 품목
    }

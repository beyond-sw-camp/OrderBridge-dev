package error.pirate.backend.item.query.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BomItemQueryDTO {

    private Long parentItemSeq;
    private String parentItemName;
    private String itemImageUrl;
    private Long childItemSeq;
    private String childItemName;
    private Integer bomChildItemQuantity;
    private String storeName;

}

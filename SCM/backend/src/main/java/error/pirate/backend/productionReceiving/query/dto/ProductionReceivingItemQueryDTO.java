package error.pirate.backend.productionReceiving.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionReceivingItemQueryDTO {
    private Long productionReceivingItemSeq;
    private Long itemSeq;
    private String itemName;
    private String itemImageUrl;
    private Long warehouseSeq;
    private String warehouseName;
    private Integer productionReceivingItemQuantity;
    private Integer productionReceivingUnitPrice;
    private String productionReceivingItemNote;
}

package error.pirate.backend.productionReceiving.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionReceivingUpdateRequest {
    private Long productionWarehouseSeq; // 생산 공장 번호
    private Long storeWarehouseSeq; // 입고 창고 번호
    private String productionReceivingName; // 생산입고명
    private Integer productionReceivingExtendedPrice; // 생산입고 총액
    private String productionReceivingNote; // 생산입고 비고
    private List<ProductionReceivingItemDTO> productionReceivingItemList;
}

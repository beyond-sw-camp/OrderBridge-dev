package error.pirate.backend.productionReceiving.query.dto;

import error.pirate.backend.warehouse.query.dto.WarehouseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductionReceivingResponse {
    private ProductionReceivingDTO productionReceivingDTO;
    List<ProductionReceivingItemQueryDTO> productionReceivingItemList;
}

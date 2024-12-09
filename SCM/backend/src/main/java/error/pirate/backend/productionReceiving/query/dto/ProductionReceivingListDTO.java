package error.pirate.backend.productionReceiving.query.dto;

import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionReceivingListDTO {
    private String productReceivingName;
    private LocalDateTime productReceivingRegDate;
    private ProductionReceivingStatus productionReceivingStatus;
    private String productionWarehouseName;
    private String storeWarehouseName;
}

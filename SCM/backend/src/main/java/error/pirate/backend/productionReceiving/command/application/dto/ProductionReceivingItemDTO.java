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
public class ProductionReceivingItemDTO {
    private Long itemSeq;
    private Integer productionReceivingItemQuantity;
    private Integer productionReceivingUnitPrice;
    private String productionReceivingItemNote;
}

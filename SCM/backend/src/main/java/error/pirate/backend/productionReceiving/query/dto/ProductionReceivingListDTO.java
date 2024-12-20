package error.pirate.backend.productionReceiving.query.dto;

import error.pirate.backend.productionReceiving.command.domain.aggregate.entity.ProductionReceivingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionReceivingListDTO {
    private Long productionReceivingSeq;
    private String productionReceivingName;
    private LocalDateTime productReceivingRegDate;
    private ProductionReceivingStatus productionReceivingStatus;
    private LocalDateTime productionReceivingReceiptDate;

    private List<ProductionReceivingItemQueryDTO> productionReceivingItemList;

    public ProductionReceivingListDTO(Long productionReceivingSeq, String productionReceivingName, LocalDateTime productReceivingRegDate, ProductionReceivingStatus productionReceivingStatus, LocalDateTime productionReceivingReceiptDate) {
        this.productionReceivingSeq = productionReceivingSeq;
        this.productionReceivingName = productionReceivingName;
        this.productReceivingRegDate = productReceivingRegDate;
        this.productionReceivingStatus = productionReceivingStatus;
        this.productionReceivingReceiptDate = productionReceivingReceiptDate;
    }
}

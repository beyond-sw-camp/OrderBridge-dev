package error.pirate.backend.productionDisbursement.query.dto;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Schema(description = "생산불출 목록")
public class ProductionDisbursementListDTO {

    private Long productionDisbursementSeq;
    private Long workOrderSeq;
    private String productionDisbursementName;
    private Long factorySeq;
    private String factoryName;
    private Long StoreSeq;
    private String StoreName;
    private LocalDateTime productionDisbursementDepartureDate;
    private ProductionDisbursementStatus productionDisbursementStatus;
    private String itemName;

}

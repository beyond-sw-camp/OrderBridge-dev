package error.pirate.backend.productionDisbursement.query.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "생산불출 현황 DTO")
public class ProductionDisbursementSituationDTO {

    private String productionDisbursementName;
    private LocalDateTime productionDisbursementDepartureDate;
    private Integer productionDisbursementTotalQuantity;
    private String factoryName;
    private String StoreName;
    private String productionDisbursementNote;

}

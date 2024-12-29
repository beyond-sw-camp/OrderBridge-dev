package error.pirate.backend.productionDisbursement.query.dto;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductionDisbursementListExcel {

    private String productionDisbursementName;  // 불출명
    private String itemName;    // 품목 이름
    private int production_disbursement_quantity;   // 품목별 불출수량
    private String StoreName;   // 원자재 창고
    private String factoryName; // 생산 공장
    private LocalDateTime productionDisbursementDepartureDate;  // 불출일
    private ProductionDisbursementStatus productionDisbursementStatus;  // 상태

}

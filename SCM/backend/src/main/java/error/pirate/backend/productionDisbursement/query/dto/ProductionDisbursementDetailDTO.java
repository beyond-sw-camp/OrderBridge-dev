package error.pirate.backend.productionDisbursement.query.dto;

import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "생산불출 상세조회 DTO")
public class ProductionDisbursementDetailDTO {

    private Long productionDisbursementSeq;
    private Long workOrderSeq;
    private String workOrderName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime workOrderDueDate;
    private Long factorySeq;
    private String factoryName;
    private String userName;
    private String productionDisbursementName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime productionDisbursementDepartureDate;
    private Integer productionDisbursementTotalQuantity;
    private ProductionDisbursementStatus productionDisbursementStatus;
    private String productionDisbursementNote;

}

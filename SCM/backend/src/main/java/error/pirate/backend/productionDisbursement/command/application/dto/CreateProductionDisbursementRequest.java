package error.pirate.backend.productionDisbursement.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "생산불출 작성요청 데이터")
public class CreateProductionDisbursementRequest {

    private Long workOrderSeq;  // 작업지시서 번호
    private LocalDateTime productionDisbursementDepartureDate;  // 생산불출 불출일
    private String productionDisbursementNote; // 생산불출 비고
    private List<ProductionDisbursementItemRequest> itemRequests;

}

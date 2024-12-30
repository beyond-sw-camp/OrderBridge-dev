package error.pirate.backend.productionDisbursement.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductionDisbursementItemRequest {
    private Long itemSeq; // 품목 ID
//    private int requiredQuantity; // 필요한 수량
    private String note; // 품목별 비고
}

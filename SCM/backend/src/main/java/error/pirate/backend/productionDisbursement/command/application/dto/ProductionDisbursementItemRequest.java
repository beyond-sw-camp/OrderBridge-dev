package error.pirate.backend.productionDisbursement.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductionDisbursementItemRequest {
    private Long itemSeq; // 품목 ID
    private Long parentItemSeq;
    private String note; // 품목별 비고
}

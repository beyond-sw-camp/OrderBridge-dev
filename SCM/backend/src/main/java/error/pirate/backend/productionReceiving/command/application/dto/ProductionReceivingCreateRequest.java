package error.pirate.backend.productionReceiving.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionReceivingCreateRequest {
    private Long userSeq; // 생산 입고 담당자 번호
    private List<Long> workOrderSeqList; // 작업지시서 번호
    private String productionReceivingName; // 생산입고명
    private Integer productionReceivingExtendedPrice; // 생산입고 총액
    private String productionReceivingNote; // 생산입고 비고
    private LocalDateTime productionReceivingReceiptDate; // 생산입고일
    private List<ProductionReceivingItemDTO> productionReceivingItemList; // 생산입고 품목
}

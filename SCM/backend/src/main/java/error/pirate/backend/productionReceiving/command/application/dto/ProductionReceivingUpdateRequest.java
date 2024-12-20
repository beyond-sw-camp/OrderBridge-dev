package error.pirate.backend.productionReceiving.command.application.dto;

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
public class ProductionReceivingUpdateRequest {
    private List<Long> workOrderSeqList; // 작업지시서 번호
    private Integer productionReceivingExtendedPrice; // 생산입고 총액
    private String productionReceivingNote; // 생산입고 비고
    private LocalDateTime productionReceivingReceiptDate;
    private List<ProductionReceivingItemDTO> productionReceivingItemList; // 생산입고 품목
}

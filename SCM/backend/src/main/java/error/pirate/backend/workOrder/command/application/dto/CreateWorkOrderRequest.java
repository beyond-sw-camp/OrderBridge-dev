package error.pirate.backend.workOrder.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "작업지시서 작성요청 데이터")
public class CreateWorkOrderRequest {

    private Long salesOrderSeq; // 주문서 번호
    private Long salesOrderItemSeq; // 주문서 품목 번호
    private Long warehouseSeq;  // 생산공장
    private LocalDateTime workOrderIndicatedDate;   // 작업지시서 지시일
    private LocalDateTime workOrderDueDate; // 작업지시서 납기일
    private String workOrderNote;   // 작업지시서 비고

}

package error.pirate.backend.workOrder.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "작업지시서 수정요청 데이터")
public class UpdateWorkOrderRequest {

    private Long salesOrderSeq; // 주문서 번호
    private Long salesOrderItemSeq; // 주문서 품목 번호
    private Long warehouseSeq;  // 생산공장
    private LocalDateTime workOrderIndicatedDate;   // 작업지시서 지시일
    private LocalDateTime workOrderDueDate; // 작업지시서 납기일
    private Integer workOrderIndicatedQuantity; // 작업지시서 지시수량
    private String workOrderNote;   // 작업지시서 비고

}
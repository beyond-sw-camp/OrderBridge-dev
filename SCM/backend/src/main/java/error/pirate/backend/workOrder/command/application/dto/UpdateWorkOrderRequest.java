package error.pirate.backend.workOrder.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(description = "작업지시서 수정요청 데이터")
public class UpdateWorkOrderRequest {

    private Long salesOrderSeq; // 주문서 번호
    private Long salesOrderItemSeq; // 주문서 품목 번호
    private Long warehouseSeq;  // 생산공장
    private LocalDate workOrderIndicatedDate;   // 작업지시서 지시일
    private LocalDate workOrderDueDate; // 작업지시서 납기일
    private int workOrderIndicatedQuantity; // 작업지시서 지시수량
    private String workOrderNote;   // 작업지시서 비고

}
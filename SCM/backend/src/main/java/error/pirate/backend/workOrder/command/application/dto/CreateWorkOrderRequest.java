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
@Schema(description = "작업지시서 요청 데이터")
public class CreateWorkOrderRequest {

    private Long workOrderSeq;  // 작업지시서 번호
    private Long salesOrderSeq; // 주문서
    private String salesOrderName;  // 주문서명
    private Long userSeq;   // 담당자
    private Long productionWarehouseSeq;  // 생산공장
    private String workOrderName;   // 작업지시서명
    private LocalDateTime workOrderRegDate; // 작업지시서 등록일
    private LocalDateTime workOrderIndicatedDate;   // 작업지시서 지시일
    private LocalDateTime workOrderEndDate; // 작업지시서 작업 종료일
    private LocalDateTime workOrderDueDate; // 작업지시서 납기일
    private String workOrderNote;   // 작업지시서 비고

}

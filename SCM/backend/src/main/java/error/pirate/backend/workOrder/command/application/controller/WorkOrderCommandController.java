package error.pirate.backend.workOrder.command.application.controller;

import error.pirate.backend.security.AuthUtil;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
import error.pirate.backend.workOrder.command.application.dto.UpdateWorkOrderRequest;
import error.pirate.backend.workOrder.command.application.service.WorkOrderService;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workOrder")
@Slf4j
@Tag(name = "Work Order", description = "작업지시서")
public class WorkOrderCommandController {

    private final WorkOrderService workOrderService;

    /* 작업지시서 등록 */
    @PostMapping
    @Operation(summary = "작업지시서 등록", description = "주문서를 불러와 품목을 선택해 작업지시서를 등록한다.")
    public ResponseEntity<Void> createWorkOrder(@Valid @RequestBody CreateWorkOrderRequest request
    ) {
        log.info("-------------- POST /api/v1/workOrder 작업지시서 등록 요청 - request:{} --------------", request);
        String userNo = AuthUtil.getAuthUser();
        workOrderService.createWorkOrderForItem(request, userNo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 작업지시서 수정 */
    @PutMapping("/{workOrderSeq}")
    @Operation(summary = "작업지시서 수정", description = "선택한 작업지시서를 수정한다.")
    public ResponseEntity<Void> updateWorkOrder(@PathVariable Long workOrderSeq,
                                                 @Valid @RequestBody UpdateWorkOrderRequest request) {
        log.info("-------------- PUT /api/v1/workOrder/{} 작업지시서 수정 요청 - request:{} --------------", workOrderSeq, request);
        String userNo = AuthUtil.getAuthUser();
        workOrderService.updateWorkOrder(workOrderSeq, request, userNo);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /* 작업지시서 상태변경(결재) */
    @PutMapping("/approval/{workOrderSeq}")
    @Operation(summary = "작업지시서 결재상태변경", description = "선택한 작업지시서의 상태를 변경한다. 결재 전 > 결재 후")
    public ResponseEntity<Void> updateWorkOrderApproval(@PathVariable Long workOrderSeq) {
        log.info("-------------- PUT /api/v1/workOrder/approval/{} 작업지시서 결재 상태 변경 요청 --------------", workOrderSeq);
        workOrderService.updateWorkOrderStatus(workOrderSeq, WorkOrderStatus.AFTER);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /* 작업지시서 상태변경(진행중) */
    @PutMapping("/ongoing/{workOrderSeq}")
    @Operation(summary = "작업지시서 진행상태변경", description = "선택한 작업지시서의 상태를 변경한다. > 진행중")
    public ResponseEntity<Void> updateWorkOrderOngoing(@PathVariable Long workOrderSeq) {
        log.info("-------------- PUT /api/v1/workOrder/ongoing/{} 작업지시서 진행 상태 변경 요청 --------------", workOrderSeq);
        workOrderService.updateWorkOrderStatus(workOrderSeq, WorkOrderStatus.ONGOING);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /* 작업지시서 상태변경(중단) */
    @PutMapping("/stop/{workOrderSeq}")
    @Operation(summary = "작업지시서 진행상태변경", description = "선택한 작업지시서의 상태를 변경한다. 진행중 > 중단")
    public ResponseEntity<Void> updateWorkOrderStop(@PathVariable Long workOrderSeq) {
        log.info("-------------- PUT /api/v1/workOrder/stop/{} 작업지시서 진행 상태 변경 요청 --------------", workOrderSeq);
        workOrderService.updateWorkOrderStatus(workOrderSeq, WorkOrderStatus.STOP);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /* 작업지시서 상태변경(완료) */
    @PutMapping("/complete/{workOrderSeq}")
    @Operation(summary = "작업지시서 진행상태변경", description = "선택한 작업지시서의 상태를 변경한다. 진행중 > 완료")
    public ResponseEntity<Void> updateWorkOrderComplete(@PathVariable Long workOrderSeq) {
        log.info("-------------- PUT /api/v1/workOrder/complete/{} 작업지시서 진행 상태 완료로 변경 요청 --------------", workOrderSeq);
        workOrderService.updateWorkOrderStatus(workOrderSeq, WorkOrderStatus.COMPLETE);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /* 작업지시서 삭제 */
    @DeleteMapping("/{workOrderSeq}")
    @Operation(summary = "작업지시서 삭제", description = "선택한 작업지시서를 삭제한다.")
    public ResponseEntity<Void> deleteWorkOrder(@PathVariable Long workOrderSeq) {
        log.info("-------------- DELETE /api/v1/workOrder/{} 작업지시서 삭제 요청 -  --------------", workOrderSeq);
        String userNo = AuthUtil.getAuthUser();
        workOrderService.deleteWorkOrder(workOrderSeq, userNo);

        return ResponseEntity.ok().build();
    }

}

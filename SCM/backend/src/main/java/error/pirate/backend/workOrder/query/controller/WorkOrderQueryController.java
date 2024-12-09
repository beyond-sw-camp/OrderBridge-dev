package error.pirate.backend.workOrder.query.controller;

import error.pirate.backend.workOrder.query.dto.*;
import error.pirate.backend.workOrder.query.service.WorkOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workOrder")
@Slf4j
@Tag(name = "Work_Order", description = "작업지시서 관리")
public class WorkOrderQueryController {

    private final WorkOrderQueryService workOrderQueryService;

    /* 목록조회 */
    @GetMapping
    @Operation(summary = "작업지시서 목록조회", description = "작업지시서 목록을 조회한다.")
    public ResponseEntity<WorkOrderListResponse> readWorkOrderList (
            @ModelAttribute WorkOrderFilterDTO filter) {
        log.info("-------------- GET /api/v1/workOrder 작업지시서 목록조회 요청 --------------");
        WorkOrderListResponse response = workOrderQueryService.readWorkOrderList(filter);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 상세조회 */
    @GetMapping("/{workOrderSeq}")
    @Operation(summary = "작업지시서 상세조회", description = "작업지시서 상세내용을 조회한다.")
    public ResponseEntity<WorkOrderResponse> readWorkOrder(@PathVariable Long workOrderSeq) {
        log.info("-------------- GET /api/v1/workOrder/{} 작업지시서 상세조회 요청 --------------", workOrderSeq);
        WorkOrderResponse response = workOrderQueryService.readWorkOrder(workOrderSeq);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 현황조회 */
    @GetMapping("/currentSituation")
    @Operation(summary = "작업지시서 현황조회", description = "작업지시서 현황을 조회한다.")
    public ResponseEntity<WorkOrderSituationResponse> readWorkOrderSituation (
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String wareHouseName
    ) {
        log.info("-------------- GET /api/v1/workOrder/currentSituation 작업지시서 현황조회 요청 --------------");
        WorkOrderSituationResponse response = workOrderQueryService.readWorkOrderSituation(startDate, endDate, clientName, wareHouseName);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 전표조회 */

}

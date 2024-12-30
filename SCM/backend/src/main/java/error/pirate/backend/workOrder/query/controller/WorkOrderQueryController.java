package error.pirate.backend.workOrder.query.controller;

import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListRequest;
import error.pirate.backend.quotation.command.domain.aggregate.entity.QuotationStatus;
import error.pirate.backend.workOrder.command.domain.aggregate.entity.WorkOrderStatus;
import error.pirate.backend.workOrder.query.dto.*;
import error.pirate.backend.workOrder.query.service.WorkOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/workOrder")
@Slf4j
@Tag(name = "Work Order", description = "작업지시서")
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
            @RequestParam(required = false) String warehouseName
    ) {
        log.info("-------------- GET /api/v1/workOrder/currentSituation 작업지시서 현황조회 요청 --------------");
        WorkOrderSituationResponse response = workOrderQueryService.readWorkOrderSituation(startDate, endDate, clientName, warehouseName);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 목록조회 엑셀 다운로드 */
    @GetMapping("/excelDownload")
    @Operation(summary = "작업지시서 목록조회 엑셀 다운로드", description = "작업지시서 목록을 엑셀로 다운로드한다.")
    public ResponseEntity<byte[]> workOrderExcelDown(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String warehouseName,
            @RequestParam(required = false) List<WorkOrderStatus> workOrderStatus
    ) {
        log.info("-------------- GET /api/v1/workOrder/excelDownload 작업지시서 엑셀 다운로드 요청 --------------");

        byte[] excelData = workOrderQueryService.readWorkOrderExcel(startDate, endDate, warehouseName, workOrderStatus);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_작업지시서.xlsx",
                StandardCharsets.UTF_8));
        httpHeaders.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

    /* 현황조회 엑셀 다운로드 */
    @GetMapping("/situation/excelDownload")
    @Operation(summary = "작업지시서 현황조회 엑셀 다운로드", description = "작업지시서 현황을 엑셀로 다운로드한다.")
    public ResponseEntity<byte[]> workOrderSituationExcelDown(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String warehouseName,
            @RequestParam(required = false) String clientName
            ) {
        log.info("-------------- GET /api/v1/workOrder/situation/excelDownload 작업지시서 현황 엑셀 다운로드 요청 --------------");

        byte[] excelData = workOrderQueryService.readWorkOrderSituationExcel(startDate, endDate, warehouseName, clientName);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_작업지시서 현황.xlsx",
                StandardCharsets.UTF_8));
        httpHeaders.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelData);
    }

}

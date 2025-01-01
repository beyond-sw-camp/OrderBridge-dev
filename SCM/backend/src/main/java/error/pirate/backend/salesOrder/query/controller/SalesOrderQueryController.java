package error.pirate.backend.salesOrder.query.controller;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.query.dto.SalesOrderItemStockStatusResponse;
import error.pirate.backend.salesOrder.query.dto.SalesOrderListResponse;
import error.pirate.backend.salesOrder.query.dto.SalesOrderResponse;
import error.pirate.backend.salesOrder.query.dto.SalesOrderSituationResponse;
import error.pirate.backend.salesOrder.query.service.SalesOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/v1/sales-order")
@Tag(name = "Sales Order", description = "주문서")
public class SalesOrderQueryController {

    private final SalesOrderQueryService salesOrderQueryService;

    @GetMapping("")
    @Operation(summary = "주문서 목록 조회")
    public ResponseEntity<SalesOrderListResponse> readSalesOrderList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) List<SalesOrderStatus> salesOrderStatus) {

        return ResponseEntity.ok(salesOrderQueryService.readSalesOrderList(
                page, size, startDate, endDate, clientName, salesOrderStatus));
    }

    @GetMapping("/{salesOrderSeq}")
    @Operation(summary = "주문서 상세 조회")
    public ResponseEntity<SalesOrderResponse> readSalesOrder(
            @PathVariable Long salesOrderSeq) {

        return ResponseEntity.ok(salesOrderQueryService.readSalesOrder(salesOrderSeq));
    }

    @GetMapping("/situation")
    @Operation(summary = "주문서 현황 조회")
    public ResponseEntity<SalesOrderSituationResponse> readSalesOrderSituation(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String clientName) {

        return ResponseEntity.ok(salesOrderQueryService.readSalesOrderSituation(
                startDate, endDate, clientName));
    }

    @GetMapping("/status")
    @Operation(summary = "주문서 상태 분류 조회")
    public ResponseEntity<List<SalesOrderStatus.SalesOrderStatusResponse>> readSalesOrderStatus() {
        return ResponseEntity.ok(SalesOrderStatus.readSalesOrderStatusList());
    }

    @GetMapping("/excel")
    @Operation(summary = "주문서 목록 엑셀 다운로드")
    public ResponseEntity<byte[]> readSalesOrderExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) List<SalesOrderStatus> salesOrderStatus) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "_주문서.xlsx"
                , StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(salesOrderQueryService.readSalesOrderExcel(startDate, endDate, clientName, salesOrderStatus));
    }

    /* 이미 작업지시가 등록된 주문서 물품 조회 */
    @GetMapping("/{salesOrderSeq}/registered-items")
    @Operation(summary = "작업지시가 등록된 주문서 물품 조회")
    public ResponseEntity<List<Long>> readRegisteredItems(@PathVariable Long salesOrderSeq) {
        List<Long> registeredItemSeqs = salesOrderQueryService.readRegisteredItems(salesOrderSeq);
        return ResponseEntity.ok(registeredItemSeqs);
    }

    /* 주문서(품목)에 대한 재고 상태 조회 */
    @GetMapping("/{salesOrderSeq}/available-quantity")
    @Operation(summary = "주문서(품목)에 대한 재고상태 조회")
    public ResponseEntity<List<SalesOrderItemStockStatusResponse>> readSalesOrderItemStockStatus(
            @PathVariable Long salesOrderSeq) {
        List<SalesOrderItemStockStatusResponse> stockStatusList = salesOrderQueryService.readSalesOrderItemStock(salesOrderSeq);
        return ResponseEntity.ok(stockStatusList);
    }

}

package error.pirate.backend.salesOrder.query.controller;

import error.pirate.backend.salesOrder.command.domain.aggregate.entity.SalesOrderStatus;
import error.pirate.backend.salesOrder.query.dto.SalesOrderListResponse;
import error.pirate.backend.salesOrder.query.dto.SalesOrderResponse;
import error.pirate.backend.salesOrder.query.dto.SalesOrderSituationResponse;
import error.pirate.backend.salesOrder.query.service.SalesOrderQueryService;
import error.pirate.backend.shippingInstruction.command.domain.aggregate.entity.ShippingInstructionStatus;import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;import java.util.List;

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
            @RequestParam(required = false) String salesOrderStatus) {

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
}

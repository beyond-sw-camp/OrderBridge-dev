package error.pirate.backend.salesOrder.query.controller;

import error.pirate.backend.salesOrder.query.dto.SalesOrderListResponse;
import error.pirate.backend.salesOrder.query.service.SalesOrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sales-order")
@Tag(name = "Sales Order", description = "주문서")
public class SalesOrderQueryController {

    private final SalesOrderQueryService salesOrderQueryService;

    @GetMapping("")
    @Operation(summary = "주문서 목록 조회")
    public ResponseEntity<SalesOrderListResponse> getSalesOrderList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String salesOrderStatus) {

        return ResponseEntity.ok(salesOrderQueryService.getSalesOrderList(
                page, size, startDate, endDate, clientName, salesOrderStatus));
    }
}

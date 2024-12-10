package error.pirate.backend.purchaseOrder.query.controller;

import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderRequest;
import error.pirate.backend.purchaseOrder.query.dto.PurchaseOrderResponsePagination;
import error.pirate.backend.purchaseOrder.query.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchaseOrder")
@Tag(name = "발주 API", description = "발주 API")
public class PurchaseOrderQueryController {

    private final PurchaseOrderService purchaseOrderService;

    @GetMapping
    @Operation(summary = "발주서 조회")
    public ResponseEntity<PurchaseOrderResponsePagination> readPurchaseOrderList(PurchaseOrderRequest purchaseOrderRequest) {
        return ResponseEntity.ok(purchaseOrderService.readPurchaseOrderList(purchaseOrderRequest));
    }

}

package error.pirate.backend.purchaseOrder.command.application.controller;

import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderCreateRequest;
import error.pirate.backend.purchaseOrder.command.application.dto.PurchaseOrderUpdateRequest;
import error.pirate.backend.purchaseOrder.command.application.service.PurchaseOrderApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchaseOrder")
@Tag(name = "Purchase Order", description = "발주")
public class PurchaseOrderController {

    private final PurchaseOrderApplicationService purchaseOrderApplicationService;

    @PostMapping
    @Operation(summary = "발주서 등록")
    public ResponseEntity<Void> createPurchaseOrder(@RequestBody PurchaseOrderCreateRequest request) {
        purchaseOrderApplicationService.createPurchaseOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    @Operation(summary = "발주서 수정")
    public ResponseEntity<Void> updatePurchaseOrder(@RequestBody PurchaseOrderUpdateRequest request) {
        purchaseOrderApplicationService.updatePurchaseOrder(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/complete/{purchaseOrderSeq}")
    @Operation(summary = "발주서 결재완료")
    public ResponseEntity<Void> updatePurchaseOrderComplete(@PathVariable Long purchaseOrderSeq) {
        purchaseOrderApplicationService.updatePurchaseOrderComplete(purchaseOrderSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/refusal/{purchaseOrderSeq}")
    @Operation(summary = "발주서 결재거절")
    public ResponseEntity<Void> updatePurchaseOrderRefusal(@PathVariable Long purchaseOrderSeq) {
        purchaseOrderApplicationService.updatePurchaseOrderRefusal(purchaseOrderSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{purchaseOrderSeq}")
    @Operation(summary = "발주서 삭제")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Long purchaseOrderSeq) {
        purchaseOrderApplicationService.deletePurchaseOrder(purchaseOrderSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

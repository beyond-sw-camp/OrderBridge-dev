package error.pirate.backend.purchase.command.application.controller;

import error.pirate.backend.purchase.command.application.dto.PurchaseCreateRequest;
import error.pirate.backend.purchase.command.application.dto.PurchaseUpdateRequest;
import error.pirate.backend.purchase.command.application.service.PurchaseApplicationService;
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
@RequestMapping("/api/v1/purchase")
@Tag(name = "Purchase", description = "구매")
public class PurchaseController {

    private final PurchaseApplicationService purchaseApplicationService;

    @PostMapping
    @Operation(summary = "구매서 등록")
    public ResponseEntity<Void> createPurchase(@RequestBody PurchaseCreateRequest request) {
        purchaseApplicationService.createPurchase(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    @Operation(summary = "구매서 수정")
    public ResponseEntity<Void> updatePurchase(@RequestBody PurchaseUpdateRequest request) {
        purchaseApplicationService.updatePurchase(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/complete/{purchaseSeq}")
    @Operation(summary = "구매서 정산완료")
    public ResponseEntity<Void> updatePurchaseComplete(@PathVariable Long purchaseSeq) {
        purchaseApplicationService.updatePurchaseComplete(purchaseSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{purchaseSeq}")
    @Operation(summary = "구매서 삭제")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long purchaseSeq) {
        purchaseApplicationService.deletePurchase(purchaseSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

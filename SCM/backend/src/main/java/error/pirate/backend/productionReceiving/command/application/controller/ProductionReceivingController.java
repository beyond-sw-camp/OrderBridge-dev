package error.pirate.backend.productionReceiving.command.application.controller;

import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingCreateRequest;
import error.pirate.backend.productionReceiving.command.application.dto.ProductionReceivingUpdateRequest;
import error.pirate.backend.productionReceiving.command.application.service.ProductionReceivingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/productionReceiving")
@Tag(name = "생산입고 API", description = "생산입고 API")
public class ProductionReceivingController {

    private final ProductionReceivingService productionReceivingService;

    @PostMapping
    @Operation(summary = "생산입고 등록")
    public ResponseEntity<Void> createProductionReceiving(@RequestBody ProductionReceivingCreateRequest request) {
        productionReceivingService.createProductionReceiving(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{productionReceivingSeq}")
    @Operation(summary = "생산입고 수정")
    public ResponseEntity<Void> updateProductionReceiving(@PathVariable Long productionReceivingSeq, @RequestBody ProductionReceivingUpdateRequest request) {
        productionReceivingService.updateProductionReceiving(productionReceivingSeq, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/approval/{productionReceivingSeq}")
    @Operation(summary = "생산입고 결재")
    public ResponseEntity<Void> updateProductionReceivingApproval(@PathVariable Long productionReceivingSeq) {
        productionReceivingService.updateProductionReceivingApproval(productionReceivingSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/complete/{productionReceivingSeq}")
    @Operation(summary = "생산입고 완료")
    public ResponseEntity<Void> updateProductionReceivingComplete(@PathVariable Long productionReceivingSeq) {
        productionReceivingService.updateProductionReceivingComplete(productionReceivingSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{productionReceivingSeq}")
    @Operation(summary = "생산입고 삭제")
    public ResponseEntity<Void> deleteProductionReceiving(@PathVariable Long productionReceivingSeq) {
        productionReceivingService.deleteProductionReceiving(productionReceivingSeq);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

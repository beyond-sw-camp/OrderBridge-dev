package error.pirate.backend.productionDisbursement.command.application.controller;

import error.pirate.backend.productionDisbursement.command.application.dto.CreateProductionDisbursementRequest;
import error.pirate.backend.productionDisbursement.command.application.service.ProductionDisbursementService;
import error.pirate.backend.workOrder.command.application.dto.CreateWorkOrderRequest;
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
@RequestMapping("/api/v1/productionDisbursement")
@Slf4j
@Tag(name = "Production Disbursement", description = "생산불출")
public class ProductionDisbursementController {

    private final ProductionDisbursementService productionDisbursementService;

    /* 생산불출 등록 */
    @PostMapping
    @Operation(summary = "생산불출 등록", description = "작업지시서를 불러와 생산불출서를 등록한다.")
    public ResponseEntity<Void> createProductionDisbursement(@Valid @RequestBody CreateProductionDisbursementRequest request
    ) {
        log.info("-------------- POST /api/v1/productionDisbursement 생산불출 등록 요청 - request:{} --------------", request);
        productionDisbursementService.createProductionDisbursement(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    /* 생산불출 삭제 */
    @DeleteMapping("/{productionDisbursementSeq}")
    @Operation(summary = "생산불출 삭제", description = "선택한 생산불출을 삭제한다.")
    public ResponseEntity<Void> deleteProductionDisbursement(@PathVariable Long productionDisbursementSeq) {
        log.info("-------------- DELETE /api/v1/productionDisbursement/{} 생산불출 삭제 요청 -  --------------", productionDisbursementSeq);
        productionDisbursementService.deleteProductionDisbursement(productionDisbursementSeq);

        return ResponseEntity.ok().build();
    }

}

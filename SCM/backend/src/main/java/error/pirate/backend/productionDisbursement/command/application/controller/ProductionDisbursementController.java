package error.pirate.backend.productionDisbursement.command.application.controller;

import error.pirate.backend.productionDisbursement.command.application.service.ProductionDisbursementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productionDisbursement")
@Slf4j
@Tag(name = "Production Disbursement", description = "생산불출")
public class ProductionDisbursementController {

    private final ProductionDisbursementService productionDisbursementService;




    /* 생산불출 삭제 */
    @DeleteMapping("/{productionDisbursementSeq}")
    @Operation(summary = "작업지시서 삭제", description = "선택한 생산불출을 삭제한다.")
    public ResponseEntity<Void> deleteProductionDisbursement(@PathVariable Long productionDisbursementSeq) {
        log.info("-------------- DELETE /api/v1/productionDisbursement/{} 생산불출 삭제 요청 -  --------------", productionDisbursementSeq);
        productionDisbursementService.deleteProductionDisbursement(productionDisbursementSeq);

        return ResponseEntity.ok().build();
    }

}

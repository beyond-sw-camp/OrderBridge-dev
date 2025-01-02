package error.pirate.backend.productionDisbursement.command.application.controller;

import error.pirate.backend.productionDisbursement.command.application.dto.CreateAndUpdateProductionDisbursementRequest;
import error.pirate.backend.productionDisbursement.command.application.service.ProductionDisbursementService;
import error.pirate.backend.productionDisbursement.command.domain.aggregate.entity.ProductionDisbursementStatus;
import error.pirate.backend.security.AuthUtil;
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
    @Operation(summary = "생산불출 등록", description = "작업지시서를 불러와 생산불출을 등록한다.")
    public ResponseEntity<Void> createProductionDisbursement(@Valid @RequestBody CreateAndUpdateProductionDisbursementRequest request
    ) {
        log.info("-------------- POST /api/v1/productionDisbursement 생산불출 등록 요청 - request:{} --------------", request);
        String userNo = AuthUtil.getAuthUser();
        productionDisbursementService.createProductionDisbursement(request, userNo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* 생산불출 수정 */
    @PutMapping("/{productionDisbursementSeq}")
    @Operation(summary = "생산불출 수정", description = "작업지시서를 불러와 생산불출을 수정한다.")
    public ResponseEntity<Void> updateProductionDisbursement(@PathVariable Long productionDisbursementSeq,
                                                             @Valid @RequestBody CreateAndUpdateProductionDisbursementRequest request) {
        log.info("-------------- PUT /api/v1/productionDisbursement/{} 생산불출 수정 요청 - request:{} --------------", productionDisbursementSeq, request);
        String userNo = AuthUtil.getAuthUser();
        productionDisbursementService.updateWorkOrder(productionDisbursementSeq, request, userNo);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /* 생산불출 삭제 */
    @DeleteMapping("/{productionDisbursementSeq}")
    @Operation(summary = "생산불출 삭제", description = "선택한 생산불출을 삭제한다.")
    public ResponseEntity<Void> deleteProductionDisbursement(@PathVariable Long productionDisbursementSeq) {
        log.info("-------------- DELETE /api/v1/productionDisbursement/{} 생산불출 삭제 요청 -  --------------", productionDisbursementSeq);
        String userNo = AuthUtil.getAuthUser();
        productionDisbursementService.deleteProductionDisbursement(productionDisbursementSeq, userNo);

        return ResponseEntity.ok().build();
    }

    /* 생산불출 상태변경(불출 후) */
    @PutMapping("/approval/{productionDisbursementSeq}")
    @Operation(summary = "생산불출 결재상태변경", description = "선택한 생산불출의 상태를 변경한다. 불출 전 > 불출 후")
    public ResponseEntity<Void> updateProductionDisbursementAfter(@PathVariable Long productionDisbursementSeq) {
        log.info("-------------- PUT /api/v1/productionDisbursement/approval/{} 작업지시서 결재 상태 변경 요청 --------------", productionDisbursementSeq);
        productionDisbursementService.updateProductionDisbursementStatus(productionDisbursementSeq, ProductionDisbursementStatus.AFTER);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

package error.pirate.backend.productionReceiving.query.controller;

import error.pirate.backend.common.ExcelDown;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListRequest;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingListResponse;
import error.pirate.backend.productionReceiving.query.dto.ProductionReceivingResponse;
import error.pirate.backend.productionReceiving.query.service.ProductionReceivingQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productionReceiving")
@Slf4j
@Tag(name = "생산입고 API", description = "생산입고 API")
public class ProductionReceivingQueryController {

    private final ProductionReceivingQueryService productionReceivingQueryService;

    @GetMapping
    @Operation(summary = "생산입고 리스트 조회")
    public ResponseEntity<ProductionReceivingListResponse> readProductionReceivingList(@ModelAttribute ProductionReceivingListRequest request, Pageable pageable) {
        log.info("readProductionReceivingList request : {}, pageable : {}", request, pageable);
        return ResponseEntity.ok(productionReceivingQueryService.readProductionReceivingList(request, pageable));
    }

    @GetMapping("/{productionReceivingSeq}")
    @Operation(summary = "생산입고 상세 조회")
    public ResponseEntity<ProductionReceivingResponse> readProductionReceiving(@PathVariable Long productionReceivingSeq) {
        return ResponseEntity.ok(productionReceivingQueryService.readProductionReceiving(productionReceivingSeq));
    }

    @GetMapping("/excelDown")
    @Operation(summary = "생산입고 엑셀다운")
    public ResponseEntity<byte[]> productionReceivingExcelDown(@ModelAttribute ProductionReceivingListRequest request, Pageable pageable) {

        return ResponseEntity.ok()
                .headers(ExcelDown.excelDownHeader("생산입고"))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(productionReceivingQueryService.productionReceivingExcelDown(request, pageable));
    }
}

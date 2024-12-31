package error.pirate.backend.productionReceiving.query.controller;

import error.pirate.backend.productionReceiving.query.dto.*;
import error.pirate.backend.productionReceiving.query.service.ProductionReceivingQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productionReceiving")
@Slf4j
@Tag(name = "Production Receiving", description = "생산입고")
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
    @Operation(summary = "생산입고 엑셀 다운")
    public ResponseEntity<byte[]> productionReceivingExcelDown(@ModelAttribute ProductionReceivingListRequest request, Pageable pageable) {

        byte[] excelData = productionReceivingQueryService.productionReceivingExcelDown(request, pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }

    @GetMapping("/situation")
    @Operation(summary = "생산입고 현황 조회")
    public ResponseEntity<List<ProductionReceivingSituationResponse>> readProductionReceivingSituation(@ModelAttribute ProductionReceivingSituationRequest request) {
        return ResponseEntity.ok(productionReceivingQueryService.readProductionReceivingSituation(request));
    }

    @GetMapping("/situation/excelDown")
    @Operation(summary = "생산입고 현황 엑셀 다운")
    public ResponseEntity<byte[]> productionReceivingSituationExcelDown(@ModelAttribute ProductionReceivingSituationRequest request) {
        byte[] excelData = productionReceivingQueryService.productionReceivingSituationExcelDown(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(excelData.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
}
